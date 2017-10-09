package tech.jpco.nazztimesheets

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Mockito.mock
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
    val mRand = Random()

    private fun createRecord(hours: Double, type: WorkSession.WorkType): WorkSession{
        val start = mFakeDate.time
        if (hours != 0.0) {
            mFakeDate.add(Calendar.MINUTE, (TimeUnit.HOURS.toMinutes(1) * hours).toInt())
            return WorkSession(start, mFakeDate.time, type)
        } else {
            return WorkSession(start,type)
        }
    }

    @Before
    fun setup() {
        mRepo = CacheOnlyRepo()
        if (mFakeDate.timeInMillis > System.currentTimeMillis() - TimeUnit.DAYS.toSeconds(1))
            mFakeDate.set(2017,1,1)
    }

    @Test fun testStartupEmpty() {
        MainPresenter(mMockView, mRepo)
        verify(mMockView).setHoursEven()
        verify(mMockView).setInactive()
    }

    private fun testStartupOpen(type: WorkSession.WorkType){
        mRepo.addNewSession(createRecord(0.0, type))
        MainPresenter(mMockView, mRepo)
        verify(mMockView).setActive(type == WorkSession.WorkType.MINION)
    }

    @Test fun testStartupOpeMinion(){
        testStartupOpen(WorkSession.WorkType.MINION)
    }

    @Test fun testStartupOpenPersonal(){
        testStartupOpen(WorkSession.WorkType.PERSONAL)
    }

    private fun flipEnum(type: WorkSession.WorkType): WorkSession.WorkType{
        when(type){
            WorkSession.WorkType.MINION -> return WorkSession.WorkType.PERSONAL
            WorkSession.WorkType.PERSONAL -> return WorkSession.WorkType.MINION
        }
    }

    private fun testStartupAheadness(type: WorkSession.WorkType): Double{
        val durations = arrayOf(1.5, -1.0, 3.25)
        durations.forEach { mRepo.addNewSession(when{
            it >= 0 -> createRecord(it, type)
            else -> createRecord(-it, flipEnum(type))
        })}
        MainPresenter(mMockView, mRepo)
        return durations.fold(0.0, {aheadness, next -> aheadness + next})
    }

    @Test fun testStartupMinionAhead(){
        verify(mMockView).setMinionAhead(
                testStartupAheadness(WorkSession.WorkType.MINION).toFloat())
    }

    @Test fun testStartupPersonalAhead(){
        verify(mMockView).setPersonalAhead(
                testStartupAheadness(WorkSession.WorkType.PERSONAL).toFloat())
    }

    private fun testNewSession(type: WorkSession.WorkType) {
        val arg: ArgumentCaptor<WorkSession> = ArgumentCaptor.forClass(WorkSession::class.java)
        val repo = mock(Repository::class.java)
        val presenter = MainPresenter(mMockView,repo)
        presenter.signIn(type)
        verify(repo).addNewSession(arg.capture())
        assertEquals(type, arg.value.type)
    }

    @Test fun testNewPersonalSession(){
        testNewSession(WorkSession.WorkType.PERSONAL)
    }
}