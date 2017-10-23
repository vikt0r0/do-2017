package ass2;

import ilog.concert.cppimpl.IloEnv;
import ilog.opl.IloOplErrorHandler;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplModelSource;

public class ass2op1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IloEnv env = new IloEnv();
		IloOplModelSource source = new IloOplModelSource(env, "ModelSparse1.mod");
		
		
		IloOplErrorHandler err = new IloOplErrorHandler(env);
		
		
		IloOplModelDefinition def = new IloOplModelDefinition(source, err);
	}

}
