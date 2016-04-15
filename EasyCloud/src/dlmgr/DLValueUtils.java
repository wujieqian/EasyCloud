package dlmgr;

import java.util.List;

import com.oracle.solaris.rad.dlmgr.DLValue;
import com.oracle.solaris.rad.dlmgr.DLValueType;

public class DLValueUtils {
	
	
	public DLValue boolDLvalue(boolean bool) {
		DLValue bool_value = new DLValue();
		bool_value.setBval(bool);
		bool_value.setType(DLValueType.BOOLEAN);
		return bool_value;
	}
	
	public DLValue boolDLvalue(List<Boolean> bool) {
		DLValue bool_value = new DLValue();
		bool_value.setBlist(bool);
		bool_value.setType(DLValueType.BOOLEANS);
		return bool_value;
	}
	
	
	public void genDLValue() {
	}

	public static void createDLDict() {
	}
	
	
}
