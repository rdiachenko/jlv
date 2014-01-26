package com.github.rd.jlv.log4j.dao;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.rd.jlv.log4j.domain.Log;
import com.github.rd.jlv.log4j.domain.LogContainer;

public class LogDaoImplTest {

	private static LogDao logDao;

	@BeforeClass
	public static void init() {
		logDao = DaoProvider.LOG_DAO.getLogDao();
		logDao.initDb();
	}

	@AfterClass
	public static void drop() {
		logDao.dropDb();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInsertNullToDb() {
		logDao.insert(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeTailingSelection() {
		logDao.insert(new Log.Builder().build());
		logDao.getTailingLogs(-1);
	}

	@Test
	public void testInsertToDb() {
		String exception = "Socket couldn't be started:\n"
				+ "java.net.SocketException: Socket closed\n"
				+ " at java.net.PlainSocketImpl.socketAccept(Native Method)\n"
				+ " at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:398)\n"
				+ "	at java.net.ServerSocket.implAccept(ServerSocket.java:530)\n"
				+ "	at java.net.ServerSocket.accept(ServerSocket.java:498)\n"
				+ "	at com.rdiachenko.jlv.log4j.socketappender.Server.start(Server.java:38)\n"
				+ "	at com.rdiachenko.jlv.ui.views.JlvViewController$2.run(JlvViewController.java:79)\n"
				+ "	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)\n"
				+ "	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)\n"
				+ "	at java.lang.Thread.run(Thread.java:724)\n"
				+ "javax.servlet.ServletException: Something bad happened\n"
				+ "at com.example.myproject.OpenSessionInViewFilter.doFilter(OpenSessionInViewFilter.java:60)\n"
				+ "at org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1157)\n"
				+ "at com.example.myproject.ExceptionHandlerFilter.doFilter(ExceptionHandlerFilter.java:28)\n"
				+ "at org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1157)\n"
				+ "at com.example.myproject.OutputBufferFilter.doFilter(OutputBufferFilter.java:33)\n"
				+ "at org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1157)\n"
				+ "at org.mortbay.jetty.servlet.ServletHandler.handle(ServletHandler.java:388)\n"
				+ "at org.mortbay.jetty.security.SecurityHandler.handle(SecurityHandler.java:216)\n"
				+ "at org.mortbay.jetty.servlet.SessionHandler.handle(SessionHandler.java:182)\n"
				+ "at org.mortbay.jetty.handler.ContextHandler.handle(ContextHandler.java:765)\n"
				+ "at org.mortbay.jetty.webapp.WebAppContext.handle(WebAppContext.java:418)\n"
				+ "at org.mortbay.jetty.handler.HandlerWrapper.handle(HandlerWrapper.java:152)\n"
				+ "at org.mortbay.jetty.Server.handle(Server.java:326)\n"
				+ "at org.mortbay.jetty.HttpConnection.handleRequest(HttpConnection.java:542)\n"
				+ "at org.mortbay.jetty.HttpConnection$RequestHandler.content(HttpConnection.java:943)\n"
				+ "at org.mortbay.jetty.HttpParser.parseNext(HttpParser.java:756)\n"
				+ "at org.mortbay.jetty.HttpParser.parseAvailable(HttpParser.java:218)\n"
				+ "at org.mortbay.jetty.HttpConnection.handle(HttpConnection.java:404)\n"
				+ "at org.mortbay.jetty.bio.SocketConnector$Connection.run(SocketConnector.java:228)\n"
				+ "at org.mortbay.thread.QueuedThreadPool$PoolThread.run(QueuedThreadPool.java:582)\n"
				+ "Caused by: com.example.myproject.MyProjectServletException\n"
				+ "at com.example.myproject.MyServlet.doPost(MyServlet.java:169)\n"
				+ "at javax.servlet.http.HttpServlet.service(HttpServlet.java:727)\n"
				+ "at javax.servlet.http.HttpServlet.service(HttpServlet.java:820)\n"
				+ "at org.mortbay.jetty.servlet.ServletHolder.handle(ServletHolder.java:511)\n"
				+ "at org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1166)\n"
				+ "at com.example.myproject.OpenSessionInViewFilter.doFilter(OpenSessionInViewFilter.java:30)\n"
				+ "... 27 more\n"
				+ "Caused by: org.hibernate.exception.ConstraintViolationException: could not insert: [com.example.myproject.MyEntity]\n"
				+ "at org.hibernate.exception.SQLStateConverter.convert(SQLStateConverter.java:96)\n"
				+ "at org.hibernate.exception.JDBCExceptionHelper.convert(JDBCExceptionHelper.java:66)\n"
				+ "at org.hibernate.id.insert.AbstractSelectingDelegate.performInsert(AbstractSelectingDelegate.java:64)\n"
				+ "at org.hibernate.persister.entity.AbstractEntityPersister.insert(AbstractEntityPersister.java:2329)\n"
				+ "at org.hibernate.persister.entity.AbstractEntityPersister.insert(AbstractEntityPersister.java:2822)\n"
				+ "at org.hibernate.action.EntityIdentityInsertAction.execute(EntityIdentityInsertAction.java:71)\n"
				+ "at org.hibernate.engine.ActionQueue.execute(ActionQueue.java:268)\n"
				+ "at org.hibernate.event.def.AbstractSaveEventListener.performSaveOrReplicate(AbstractSaveEventListener.java:321)\n"
				+ "at org.hibernate.event.def.AbstractSaveEventListener.performSave(AbstractSaveEventListener.java:204)\n"
				+ "at org.hibernate.event.def.AbstractSaveEventListener.saveWithGeneratedId(AbstractSaveEventListener.java:130)\n"
				+ "at org.hibernate.event.def.DefaultSaveOrUpdateEventListener.saveWithGeneratedOrRequestedId(DefaultSaveOrUpdateEventListener.java:210)\n"
				+ "at org.hibernate.event.def.DefaultSaveEventListener.saveWithGeneratedOrRequestedId(DefaultSaveEventListener.java:56)\n"
				+ "at org.hibernate.event.def.DefaultSaveOrUpdateEventListener.entityIsTransient(DefaultSaveOrUpdateEventListener.java:195)\n"
				+ "at org.hibernate.event.def.DefaultSaveEventListener.performSaveOrUpdate(DefaultSaveEventListener.java:50)\n"
				+ "at org.hibernate.event.def.DefaultSaveOrUpdateEventListener.onSaveOrUpdate(DefaultSaveOrUpdateEventListener.java:93)\n"
				+ "at org.hibernate.impl.SessionImpl.fireSave(SessionImpl.java:705)\n"
				+ "at org.hibernate.impl.SessionImpl.save(SessionImpl.java:693)\n"
				+ "at org.hibernate.impl.SessionImpl.save(SessionImpl.java:689)\n"
				+ "at sun.reflect.GeneratedMethodAccessor5.invoke(Unknown Source)\n"
				+ "at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n"
				+ "at java.lang.reflect.Method.invoke(Method.java:597)\n"
				+ "at org.hibernate.context.ThreadLocalSessionContext$TransactionProtectionWrapper.invoke(ThreadLocalSessionContext.java:344)\n"
				+ "at $Proxy19.save(Unknown Source)\n"
				+ "at com.example.myproject.MyEntityService.save(MyEntityService.java:59) <-- relevant call (see notes below)\n"
				+ "at com.example.myproject.MyServlet.doPost(MyServlet.java:164)\n"
				+ "... 32 more\n"
				+ "Caused by: java.sql.SQLException: Violation of unique constraint MY_ENTITY_UK_1: duplicate value(s) for column(s) MY_COLUMN in statement [...]\n"
				+ "at org.hsqldb.jdbc.Util.throwError(Unknown Source)\n"
				+ "at org.hsqldb.jdbc.jdbcPreparedStatement.executeUpdate(Unknown Source)\n"
				+ "at com.mchange.v2.c3p0.impl.NewProxyPreparedStatement.executeUpdate(NewProxyPreparedStatement.java:105)\n"
				+ "at org.hibernate.id.insert.AbstractSelectingDelegate.performInsert(AbstractSelectingDelegate.java:57)\n"
				+ "... 54 more\n";

		Log log = (new Log.Builder()).categoryName("category")
				.className("LogDaoImpl")
				.date("26-Aug-2013-01:38:52:165")
				.fileName("file")
				.locationInfo("locInfo")
				.lineNumber("line")
				.methodName("method")
				.level("INFO")
				.ms("12578945621366548875")
				.threadName("main[0]-$someUnknownThread")
				.message("Selection -1 tailing logs from db...['] \nn && some symbols ? )\"\t/**-? ololo''$")
				.throwable(exception)
				.ndc("ndc1 ndc2")
				.mdc("{test1=testValue1, test2=testValue2, test3=testValue3}")
				.build();

		logDao.insert(log);
		LogContainer logs = logDao.getTailingLogs(1);
		Log insertedLog = logs.get();

		Assert.assertEquals(log, insertedLog);
	}
}
