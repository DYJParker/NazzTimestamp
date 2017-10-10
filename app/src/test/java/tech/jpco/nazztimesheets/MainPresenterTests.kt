package tech.jpco.nazztimesheets

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.mockito.AdditionalMatchers.*
import org.mockito.junit.MockitoJUnitRunner
import tech.jpco.nazztimesheets.mainscreen.MainContract
import tech.jpco.nazztimesheets.mainscreen.MainPresenter
import tech.jpco.nazztimesheets.model.CacheOnlyRepo
import tech.jpco.nazztimesheets.model.Repository
import tech.jpco.nazztimesheets.model.WorkSession
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Dave - Work on 10/9/2017.
 */


@RunWith(MockitoJUnitRunner::class)
class MainPresenterTests {
    val mMockView = mock(MainContract.View::class.java)
    lateinit var mRepo: Repository
    val mFakeDate: Calendar = Calendar.getInstance()

    private fun createRecord(hours: Double, type: WorkSession.WorkType): WorkSession {
        val start = mFakeDate.time
        if (hours != 0.0) {
            mFakeDate.add(Calendar.MINUTE, (TimeUnit.HOURS.toMinutes(1) * hours).toInt())
            return WorkSession(start, mFakeDate.time, type)
        } else {
            return WorkSession(start, type)
        }
    }

    @Before
    fun setup() {
        mRepo = CacheOnlyRepo()
        mFakeDate.set(2017, 1, 1)
    }

    @Test
    fun testStartupEmpty() {
        MainPresenter(mMockView, mRepo)
        verify(mMockView).setHoursEven()
        verify(mMockView).setInactive()
    }

    private fun testStartupOpen(type: WorkSession.WorkType) {
        mRepo.addNewSession(createRecord(0.0, type))
        MainPresenter(mMockView, mRepo)
        verify(mMockView).setActive(type == WorkSession.WorkType.MINION)
    }

    @Test
    fun testStartupOpeMinion() {
        testStartupOpen(WorkSession.WorkType.MINION)
    }

    @Test
    fun testStartupOpenPersonal() {
        testStartupOpen(WorkSession.WorkType.PERSONAL)
    }

    private fun flipEnum(type: WorkSession.WorkType): WorkSession.WorkType {
        when (type) {
            WorkSession.WorkType.MINION -> return WorkSession.WorkType.PERSONAL
            WorkSession.WorkType.PERSONAL -> return WorkSession.WorkType.MINION
        }
    }

    private fun testStartupAheadness(type: WorkSession.WorkType): Double {
        val durations = arrayOf(1.5, -1.2, 3.25)
        durations.forEach {
            mRepo.addNewSession(when {
                it >= 0 -> createRecord(it, type)
                else -> createRecord(-it, flipEnum(type))
            })
        }
        MainPresenter(mMockView, mRepo)
        return durations.fold(0.0, { aheadness, next -> aheadness + next })
    }

    @Test
    fun testStartupMinionAhead() {
        testStartupAheadness(WorkSession.WorkType.MINION)
        verify(mMockView).setMinionAhead(3.5f)
    }

    @Test
    fun testStartupPersonalAhead() {
        testStartupAheadness(WorkSession.WorkType.PERSONAL)
        verify(mMockView).setPersonalAhead(3.5f)
    }

    private fun testNewSession(type: WorkSession.WorkType) {
        val arg: ArgumentCaptor<WorkSession> = ArgumentCaptor.forClass(WorkSession::class.java)
        val repo = mock(Repository::class.java)
        val presenter = MainPresenter(mMockView, repo)
        presenter.signIn(type)
        verify(repo).addNewSession(arg.capture())
        assertEquals(type, arg.value.type)
        val time = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(10)
        assertTrue(arg.value.start.time > time
                && arg.value.start.time < System.currentTimeMillis())
        assertNull(arg.value.end)
    }

    @Test
    fun testNewPersonalSession() = testNewSession(WorkSession.WorkType.PERSONAL)

    @Test
    fun testNewMinionSession() = testNewSession(WorkSession.WorkType.MINION)

    @Test(expected = IllegalStateException::class)
    fun testSignOutNoActiveSession() {
        val presenter = MainPresenter(mMockView, mRepo)
        mRepo.addNewSession(createRecord(1.0, WorkSession.WorkType.PERSONAL))
        presenter.signOut()
    }

    @Test
    fun testSignOut() {
        mRepo.addNewSession(createRecord(1.0, WorkSession.WorkType.PERSONAL))
        mRepo.addNewSession(createRecord(0.0,WorkSession.WorkType.MINION))
        val presenter = MainPresenter(mMockView, mRepo)
        presenter.signOut()
        verify(mMockView).setInactive()
        verify(mMockView).setMinionAhead(gt(0.0f))
    }
}