package org.callas.parse;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.processes.Code;
import org.callas.absyn.processes.Let;
import org.callas.absyn.processes.Variable;
import org.callas.absyn.types.*;
import org.callas.core.IFileLoader;
import org.callas.util.TypeSubstitution;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.errorMsg.msgs.SyntacticError;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.util.Pair;

class ProcessGrmUtil {
	/**
	 * The extension of files that conform to the callas type language.
	 */
	private static final String TYPE_EXT = ".caltype";
	
	private final List<ErrorMessage> errors;
	private final IFileLoader fileLoader;
    private Map<Symbol, RecursiveType> types;
	

	public ProcessGrmUtil(List<ErrorMessage> errors, IFileLoader fileLoader) {
		this.errors = errors;
		this.fileLoader = fileLoader;
		this.types = new TreeMap<Symbol, RecursiveType>();
	}
	
    public void setType(RecursiveType t) {
        if (types.containsKey(t.variable.name)) {
            errors.add(new SyntacticError(t.getSourceLocation(), "Type '" + t.variable + "' is already defined."));
        }
        types.put(t.variable.name, t);
    }
    
    public CallasType getType(SourceLocation loc, org.tyco.common.symbol.Symbol name) {
        CallasType type = types.get(name);
        if (type == null) {
            return new TypeVariable(loc, name);
        }
        return type;
    }
    
    public void updateImported(Map<Symbol, RecursiveType> importedTypes) {
    	for (RecursiveType type : importedTypes.values()) {
    		setType(type);
    	}
    }
    
    public static String fromModuleToFile(String resource) {
    	String prefix = "";
		String resourceWithoutDots = resource.replaceAll("^\\.+", "");
		String dots = resource.substring(0, resource.length() - resourceWithoutDots.length());
		for (int i = 0; i < dots.length(); i++) {
			prefix += ".." + File.separator;
		}
		resource = resourceWithoutDots;
        resource = resource.replace(".", File.separator);
        return prefix + resource;
    }
    
    public Map<Symbol, RecursiveType> importTypes(SourceLocation loc, String resource) {
    	resource = fromModuleToFile(resource) + TYPE_EXT;
        try {
            ProcessGrm grm = new ProcessGrm(errors, resource, fileLoader.loadFile(resource), fileLoader);
            try {
                grm.parse();
                return grm.getTypes();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            errors.add(new SyntacticError(loc, e.toString()));
        }
        return new HashMap<org.tyco.common.symbol.Symbol, RecursiveType>();
    }
    
    public Map<Symbol, RecursiveType> importTypes(SourceLocation loc, String resource, List<Pair<Symbol,Symbol>> importAsPairs) {
    	Map<Symbol, RecursiveType> result = new TreeMap<Symbol, RecursiveType>();
    	Map<Symbol, RecursiveType> imported = importTypes(loc, resource);
    	for (Pair<Symbol, Symbol> importAs : importAsPairs) {
    		RecursiveType importedType = imported.get(importAs.getLeft());
    		if (importedType == null) {
    			errors.add(new SyntacticError(loc, String.format("Imported type '%s' does not exist.", importAs.getLeft())));
    			continue;
    		}
    		TypeVariable asVar = new TypeVariable(importAs.getRight());
			TypeSubstitution sigma = new TypeSubstitution(new TypeVariable(importAs.getLeft()), asVar);
    		CallasType replaced = sigma.replace(importedType.type);
			RecursiveType newRec = new RecursiveType(asVar, replaced);
    		result.put(importAs.getRight(), newRec);
    	}
    	return result;
    }
    
	public void assemble(SourceLocation loc, Symbol selfType, Map<Symbol, FunctionType> body, List<CallasType> bodies) {
		for (Entry<Symbol, FunctionType> entry : body.entrySet()) {
			FunctionType func = entry.getValue();
			List<CallasType> extendingTypes = new ArrayList<CallasType>(func.parameters.size() + 1);
			extendingTypes.add(new TypeVariable(selfType));
			extendingTypes.addAll(func.parameters);
			entry.setValue(new FunctionType(func.getSourceLocation(), extendingTypes, func.result));
		}
		for (CallasType type : bodies) {
			if (!(type instanceof RecursiveType)) {
				continue;
			}
			RecursiveType rec = (RecursiveType) type;
			final Map<Symbol, FunctionType> otherBody;
			otherBody = ((CodeType) rec.type).functions;
			TypeSubstitution sigma = new TypeSubstitution(rec.variable, new TypeVariable(selfType));
			for (Entry<Symbol, FunctionType> entry : otherBody.entrySet()) {
				if (body.containsKey(entry.getKey())) {
					String msg = String.format("Extended module %s redefines " +
							"previously defined function %s.", rec.variable, entry.getKey());
					errors.add(new SyntacticError(loc, msg));
				} else {
					body.put(entry.getKey(), (FunctionType) sigma.replace(entry.getValue()));
				}
			}
		}
	}

	/**
	 * @return the types
	 */
	public Map<Symbol, RecursiveType> getTypes() {
		return types;
	}
	
	public static CallasProcess composeBody(CallasProcess...body) {
		return composeBody(Arrays.asList(body));
	}
	
	public static CallasProcess composeBody(List<CallasProcess> body) {
		if (body.isEmpty()) {
			return Code.NIL;
		}
		
		Collections.reverse(body);
		Iterator<CallasProcess> iterator = body.iterator();
		CallasProcess result = iterator.next();
		if (result instanceof Assignment) {
			Assignment assign = (Assignment) result;
			result = new Let(assign.variable, assign.value, assign.variable);
		}
		
		while (iterator.hasNext()) {
			final CallasProcess proc = iterator.next();
			final Variable name;
			final CallasProcess value;
			if (proc instanceof Assignment) {
				Assignment assign = (Assignment) proc;
				name = assign.variable;
				value = assign.value;
			} else {
				// do not forget the source code location
				name = new Variable(proc.getSourceLocation(), Symbol.symbol(";"));
				value = proc;
			}
			result = new Let(name, value, result);
		}
		return result;
	}
}
