package com.cristiano.java.bpM.entidade.functions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.cristiano.java.bpM.utils.DomainSpecificFunctionHelper;
import com.cristiano.java.bpM.utils.FunctionHelper;
import com.cristiano.java.product.extras.FunctionConsts;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.proc.noise.IMakeNoise;
import com.cristiano.utils.Log;

public class FunctionSolver {
	HashMap<String, FunctionGroup> solvers = new HashMap<String, FunctionGroup>();
	final HashMap<Integer, IMakeNoise> noiseSolvers = new HashMap<Integer, IMakeNoise>();

	public FunctionSolver() {
		initSolvers();
	}

	private void initSolvers() {
		addTrigonometricFunctions();
		addDomainSpecificFunctions();

		addFunction(FunctionConsts.FUNCTION_RAND, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoRand(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});

		addFunction(FunctionConsts.FUNCTION_RANDF, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoRandf(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		addFunction(FunctionConsts.FUNCTION_SIMPLEX_2D, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoSimplexNoise2d(texto, fromFunction, noiseSolvers);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_SIMPLEX_3D, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoSimplexNoise3d(texto, fromFunction, noiseSolvers);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});

		addFunction(FunctionConsts.FUNCTION_CONCAT, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoConcat(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_SQRT, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoSqrt(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		addFunction(FunctionConsts.FUNCTION_SQR, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoSqr(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		addFunction(FunctionConsts.FUNCTION_HEX2DEC, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoHex2Dec(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});

		addFunction(FunctionConsts.FUNCTION_PICKONE, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoPickOne(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_PICKSINGLE, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoPickSingle(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_RANDOM_TAG, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoRandomTag(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});

		addFunction(FunctionConsts.FUNCTION_PICKFROMLIST, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoPickFromList(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});

		addFunction(FunctionConsts.FUNCTION_PICKFINAL, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoPickFinal(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_HEX, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoHex(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_MOD, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoMod(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});

		addFunction(FunctionConsts.FUNCTION_COUNT, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoCount(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_LIST, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoList(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_IS_EMPTY, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoIsEmpty(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_ABS, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoAbs(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});
		addFunction(FunctionConsts.FUNCTION_LOG, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoLog(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});

		addFunction(FunctionConsts.FUNCTION_ABSF, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoAbsF(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		addFunction(FunctionConsts.FUNCTION_IS_TOP_QUADRANT, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoTOPQUADRANT(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		
		addFunction(FunctionConsts.FUNCTION_ROUND, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoRound(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});


		addFunction(FunctionConsts.FUNCTION_SUM, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoSum(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});

		addFunction(FunctionConsts.FUNCTION_IF, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				String saida = FunctionHelper.resolveFuncaoIF(FunctionConsts.FUNCTION_IF, texto, fromFunction);
				return saida;
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});

	}

	private void addDomainSpecificFunctions() {
		addFunction(FunctionConsts.FUNCTION_ADD_MESH, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return DomainSpecificFunctionHelper.resolveFuncaoAddMesh(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return true;
			}
		});

	}

	private void addTrigonometricFunctions() {
		addFunction(FunctionConsts.FUNCTION_SIN_RADIAN, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoSinR(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		addFunction(FunctionConsts.FUNCTION_COS_RADIAN, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoCosR(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		addFunction(FunctionConsts.FUNCTION_TAN_RADIAN, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoTanR(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		addFunction(FunctionConsts.FUNCTION_SIN_DEGREE, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoSinD(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		addFunction(FunctionConsts.FUNCTION_COS_DEGREE, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoCosD(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
		addFunction(FunctionConsts.FUNCTION_TAN_DEGREE, new IFunctionSolving() {
			@Override
			public String solveFunction(String texto, Solver fromFunction) {
				return FunctionHelper.resolveFuncaoTanD(texto, fromFunction);
			}

			@Override
			public boolean canSolve(String params) {
				return FunctionHelper.areParamsNumeric(params);
			}
		});
	}

	public String solve(String function, String texto, Solver fromFunction) {
		IFunctionSolving func = solvers.get(function);
		if (func == null)
			return "";
		String ret = function + "(" + texto + ")";
		try {
			if (func.canSolve(texto)) {
				ret = func.solveFunction(texto, fromFunction);
			}
			int x;
		} catch (Exception e) {
			Log.fatal("Error Parsing function:"+ret+" exception:"+e);
			e.printStackTrace();
		}
		return ret;
	}

	public String resolveFuncoes(String property, Solver solver) {
		String prop = property;

		// for each function, the value is calculated...
		Iterator<Entry<String, FunctionGroup>> it = solvers.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, FunctionGroup> pairs = (Map.Entry<String, FunctionGroup>) it.next();
			String functionName = pairs.getKey();
			property = extractFunction(property, functionName, solver);
		}
		if (!prop.replace(" ", "").equals(property.replace(" ", "")))
			return solver.resolveFunctionOf(property, solver.lastParam);
		return property;
	}

	private String extractFunction(String texto, String nameFunction, Solver solver) {
		int pars = 1;

		String lastTry = "";
		while ((texto.contains(nameFunction + "(")) && (!lastTry.equals(texto))) {
			lastTry = texto;
			texto = extractFunctionDefinida(texto, nameFunction, pars, solver);
		}
		return texto;

	}

	private String extractFunctionDefinida(String texto, String nameFunction, int pars, Solver solver) {
		String saida = "";
		int pos = texto.indexOf(nameFunction);
		int posFim = 0;
		// determino a string da funcao
		for (int i = pos + nameFunction.length() + 1; i < texto.length(); i++) {
			if (texto.charAt(i) == '(') {
				pars++;
			}
			if (texto.charAt(i) == ')') {
				pars--;
			}

			if (pars == 0) {
				posFim = i;
				break;
			}
			saida = saida + texto.charAt(i);
		}

		// monto a fun��o resolvida
		if (!saida.equals("")) {
			saida = extraParametrosDaFuncao(nameFunction, saida, solver);
		}

		String antes = texto.substring(0, pos);
		String depois = texto.substring(posFim + 1);
		saida = antes + resolveFuncaoSimples(nameFunction, saida, solver) + depois;
		return saida;
	}

	private String extraParametrosDaFuncao(String nameFunction, String saida, Solver solver) {
		String[] params = solver.getParams(saida);
		saida = nameFunction + "(";
		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				params[i] = solver.resolveFunctionOf(params[i], solver.lastParam);
				saida += "," + params[i];
			}
		}
		saida += ")";
		saida = saida.replace("(,", "(");
		return saida;
	}

	private String resolveFuncaoSimples(String function, String texto, Solver solver) {
		try {

			if (!texto.equals("")) {
				texto = texto.substring(function.length() + 1, texto.length() - 1);
			}
			String[] params = StringHelper.getParams(texto);
			texto = "";
			for (int i = 0; i < params.length; i++) {
				params[i] = solver.resolveFunctionOf(params[i], solver.lastParam);
				texto += params[i] + ",";
			}

			// removing the extra ','
			if (texto.length() > 0) {
				texto = texto.substring(0, texto.length() - 1);
			}
			// texto = solver.resolveFunctionOf(texto);

			return solve(function, texto, solver);
		} catch (Exception e) {
			Log.error("Exception solving: " + function + "(" + texto + ")");
			e.printStackTrace();
			return function + "(" + texto + ")";
		}
	}

	public void addUserFunction(String function, String params, String val) {
		UserFunction userFunction = new UserFunction(function, params, val);
		addFunction(function, userFunction);
	}

	private void addFunction(String functionName, IFunctionSolving function) {
		FunctionGroup group = getGroupFor(functionName);
		group.addFunction(function);
	}

	private FunctionGroup getGroupFor(String functionName) {
		FunctionGroup group = solvers.get(functionName);
		if (group == null) {
			group = new FunctionGroup();
			solvers.put(functionName, group);
		}
		return group;
	}

}
