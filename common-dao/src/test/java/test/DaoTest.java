package test;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.quxiqi.common.dao.bean.Mapper;
import com.quxiqi.common.dao.core.CommonDao;
import com.quxiqi.common.dao.core.Configrution;


public class DaoTest {
	
	@Test
	public void daoTest() throws Exception {
		
		InputStream inputStream = Resources.getResourceAsStream("test/mybatis-config.xml");
		
		SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
		
		Configrution conf = Configrution.getInstance(build.openSession());
		
		CommonDao commonDao = conf.getCommonDao();
		try{
			commonDao.execute("test/TestMapper.xml", "select", new Mapper(),Mapper.class , Mapper.class);
		}catch(Exception e){
			System.out.println(System.currentTimeMillis());
			try{
				commonDao.execute("test/TestMapper.xml", "select", new Mapper(),Mapper.class , Mapper.class);
			}catch(Exception eSub){
				System.out.println(System.currentTimeMillis());
			}
		}
//		System.out.println(execute);
	}
}
