package com.yzl.study.db2rest.component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.yzl.study.db2rest.utils.FieldNameConvertor;

@Component
public class DbMetaInfo {
	
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private List<TableInfo> list = new ArrayList<TableInfo>();
	public  static Map<String,TableInfo> tableMap = new ConcurrentHashMap<String,TableInfo>();
	
	
	@PostConstruct
	public  void init() {
		
		try {
			DatabaseMetaData metaData = namedParameterJdbcTemplate.getJdbcTemplate().getDataSource().getConnection().getMetaData();

			ResultSet rs = metaData.getTables(null, null, null,new String[] {"TABLE","VIEW"}) ;//  .getPrimaryKeys(null, null, "EMPLOYEE");
			
			while(rs.next()){
		        String tableName = rs.getString("TABLE_NAME");
		        String tableType = rs.getString("TABLE_TYPE");
		        TableInfo ti = new TableInfo();
		        ti.setTableName(tableName);
		        ti.setTableObjectName(FieldNameConvertor.columnName2FieldName(tableName));
		        ti.setTableType(tableType);
		    	ResultSet columns = metaData.getColumns(null, null, "t_project_type", null);
		    	while(columns.next()){
			        String colName=columns.getString(4); 
			        ti.getColumnList().add(colName);
			    }
		        list.add(ti);
		        
		    }
			
			TreeMap<Integer,String> tMap ;
			for(TableInfo t:list) {
				ResultSet rs2 = metaData.getPrimaryKeys(null, null, t.getTableName());
				tMap = new TreeMap<Integer,String>();
				while(rs2.next()){
			        String tableName = rs2.getString("TABLE_NAME");
			        String colName = rs2.getString("COLUMN_NAME");
			        String KEY_SEQ  = rs2.getString("KEY_SEQ");
			        tMap.put(Integer.valueOf(KEY_SEQ), colName);
			    }
				if(tMap.size()>0) {
					t.setHavePk(true);
					if(tMap.size()==1) {
					
						t.setSinglePkColName(tMap.get(1));
					}else if(tMap.size()>1) {
						Iterator<Integer> iterator = tMap.keySet().iterator();
						while(iterator.hasNext()) {
							
							Integer k = iterator.next();
							t.getPkColName().add(tMap.get(k));						
						}
						
					}
					
					
				}
				
				tableMap.put(t.getTableObjectName(), t);
				
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	public static boolean checkTableExist(String tableObjectName){
		
		TableInfo tableInfo = DbMetaInfo.tableMap.get(tableObjectName);
		return  tableInfo!=null ;
		
	}
	public static boolean checkColumnExist(String tableObjectName,String columnName){
		
		boolean exist = false;
		TableInfo tableInfo = DbMetaInfo.tableMap.get(FieldNameConvertor.columnName2FieldName(tableObjectName)   );
		if(tableInfo!=null) {
			
			List<String> columnList = tableInfo.getColumnList();
			for(String s:columnList) {
				//这里忽略大小写 TODO   最好根据不同数据库设置 来判断  
				if( s.equalsIgnoreCase(columnName) ) {
					exist = true ;
				}
				
			}
			
		}  
		return  exist ;
		
	}

}
