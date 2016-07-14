package com.hansol.iswebsocket.dbservice.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;

import com.hansol.iswebsocket.dbservice.config.ConnectionFactory;
import com.hansol.iswebsocket.dbservice.dao.DbSvcDAO;

//TODO Interface 만들기
public class DbSvcServiceImpl {
	
//	From : https://mybatis.github.io/mybatis-3/ko/java-api.html
//	JDK 1.7이상과 마이바티스 3.2이상을 사용한다면 다음처럼 try-with-resources 구문을 사용할 수 있다.
//
//	try (SqlSession session = sqlSessionFactory.openSession()) {
//	    // following 3 lines pseudocode for "doing some work"
//	    session.insert(...);
//	    session.update(...);
//	    session.delete(...);
//	    session.commit();
//	}
	
	private volatile static DbSvcServiceImpl instance;

	public static DbSvcServiceImpl getInstance()
	{
		if (instance == null)
		{
			synchronized (DbSvcServiceImpl.class)
			{
				if (instance == null)
					instance = new DbSvcServiceImpl();
			}
		}

		return instance;
	}
	
	public String getSqlBySvcNo(Map paramMap) throws Exception {
		SqlSession session = ConnectionFactory.getSession().openSession();
		DbSvcDAO dao =session.getMapper(DbSvcDAO.class);
			String sql = dao.getSqlBySvcNo(paramMap);
		session.close();
		return sql;
	} 
	
	
	public List<Map<String,Object>> getListParam(Map paramMap) throws Exception {
		SqlSession session = ConnectionFactory.getSession().openSession();
		DbSvcDAO dao =session.getMapper(DbSvcDAO.class);
			List<Map<String,Object>> list = dao.getListParam(paramMap);
		session.close();
		return list;
	}
	
	public boolean doUpdate(Map paramMap) {
		SqlSession session = ConnectionFactory.getSession().openSession();
		try {
			DbSvcDAO dao =session.getMapper(DbSvcDAO.class);
			dao.doUpdateList(paramMap);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			return false;
		} finally {
			session.close();
		}
		return true;
	}
	
	// List Insert/Update/Delete Using Batch 
	// TODO From : http://amitsavm.blogspot.kr/2011/11/mybatis-batch-update.html
	public boolean doInsert(List<Map<String,Object>> paramList) {
		if(!paramList.isEmpty()) {
			SqlSession session = ConnectionFactory.getSession().openSession(ExecutorType.BATCH, false);
			try {
				DbSvcDAO dao =session.getMapper(DbSvcDAO.class);
				for(Map<String,Object> paramMap : paramList) {
					dao.doInsertList(paramMap);
				}
				
				// session에 담긴 캐쉬를 db에 동기화 시킴
				session.flushStatements();
				
				session.commit();
			} catch (Exception e) {
				session.rollback();
				e.printStackTrace();
				return false;
			} finally {
				session.close();
			}
			return true;
		} else {
			return false;
		}
	}
	
	public Map<String,Object> doProcedure(Map<String,Object> paramMap) {
		Map<String,Object> result = null;
		SqlSession session = ConnectionFactory.getSession().openSession();
		try {
			DbSvcDAO dao =session.getMapper(DbSvcDAO.class);
			result = dao.doProcedure(paramMap);
			session.commit();
		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}
		return result;
	}
}
