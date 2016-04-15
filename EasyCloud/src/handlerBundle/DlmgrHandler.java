package handlerBundle;
import com.oracle.solaris.rad.client.*;
import com.oracle.solaris.rad.connect.*;
import com.oracle.solaris.rad.dlmgr.DatalinkManager;
import com.oracle.solaris.rad.zonemgr.*;
import com.oracle.solaris.rad.dlmgr.DLDict;
import com.oracle.solaris.rad.dlmgr.DLValue;
import com.oracle.solaris.rad.dlmgr.DLValueType;
import com.oracle.solaris.rad.dlmgr.VNIC;

import connection.ConnContainer;

import org.apache.log4j.Logger; 


import java.util.*;
import java.io.*;

/**  
*   
* 项目名称：EasyCloud  
* 类名称：DlmgrHandler  
* 类描述：  
* 创建人：jieqwu  
* 创建时间：2016年4月15日 上午10:53:08  
* @version  0.1     
*/ 
public class DlmgrHandler {
	private Connection con = null;
	private DatalinkManager dm_obj;
	private Logger logger;
	
	public DlmgrHandler(Connection con) throws RadException, IOException {
		this.con = con;
		dm_obj=con.getObject(new DatalinkManager());
		this.logger=Logger.getLogger(DlmgrHandler.class);
	}
	
	public void dladm_show() {
		
	}
	public VNIC getVNIC(String vnic_name) throws RadException, IOException {
		
		for (ADRName name : this.con.listObjects(new VNIC())){
			VNIC vnic = con.getObject(name);
			
			if (vnic.getName().equals(vnic_name))
				return vnic;
		}
		this.logger.log(null, "Doesn't found vnic: " + vnic_name);
		return null;
	}
	
	
	public static Map<String, DLValue> create_properties(Map<String, String> source){
		
		return null;
	}

	public static void create_vnic(Connection con, 
								   String vnic_name, 
								   Map<String, DLValue> properties ) {
		try {
			DatalinkManager dm_obj = con.getObject(new DatalinkManager());
			
			
			
		} catch (RadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception {

	}
	public void test_create_vnic() throws RadException, IOException {
		Connection con = ConnContainer.getConnection("sct-x42-18");
		DatalinkManager dm_obj=con.getObject(new DatalinkManager());

		DLValue vnic1_lower_link=new DLValue();
		vnic1_lower_link.setSval("net3");
		vnic1_lower_link.setType(DLValueType.STRING);
		Map<String, DLValue> properties=new HashMap<String, DLValue>();
		properties.put("lower-link", vnic1_lower_link);
		VNIC vnic1;
		try {
			vnic1=dm_obj.createVNIC("vnic1", properties);
			System.out.println("create vnic vnic1");
		}catch (Exception e) {
			e.printStackTrace();
		}

		List<ADRName> vnicList = con.listObjects(new VNIC());
		vnic1 = con.getObject(vnicList.get(0));
	
		Map<String, DLValue> vnic_info = vnic1.getInfo(null);
		
		for (Map.Entry<String, DLValue> entry : vnic_info.entrySet() ){
			System.out.format("Key =　%s, Value = %s \n", entry.getKey().toString(), entry.getValue().getSval());
		}
		//dm_obj.deleteVNIC("vnic1",null);
	}
	
}
