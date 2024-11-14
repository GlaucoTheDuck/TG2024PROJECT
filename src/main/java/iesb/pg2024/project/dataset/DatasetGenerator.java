package iesb.pg2024.project.dataset;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DatasetGenerator {	
	public static final String DIRECTORY_HIERACHY = "Hierarquia de Diretório";
	public static final String EXTEND_HIERACHY = "Extend";
	public static final String IMPLEMENT_HIERACHY = "Implement";
	public static final String ARCHIVE_HIERACHY = "Hierarquia em Arquivo";
	
	static BufferedWriter bw;
	
	public DatasetGenerator() {
		File arquivo = new File("C:\\Users\\maisg\\eclipse-workspace\\iesb_pg2024_project\\src\\main\\java\\iesb\\pg2024\\project\\dataset\\arestas.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(arquivo, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    this.bw = new BufferedWriter(fw);
	}
    
	
	public static Map<String, List<String>> directoryHierachy = new Hashtable<>();
	public static Map<String, List<String>> aresta = new Hashtable<>();
	
	public static void addAresta(String from, String to, long weight, String description) {
		
		if(!aresta.containsKey(from)) {
        	aresta.put(from, new ArrayList<String>());
        }
        if(aresta.get(from).contains(to)) {
        	System.out.println("[Warning] >> Aresta já adicionada");
        	return;
        }
		
        
        try {
            if(weight == -1) {
            	if(description == IMPLEMENT_HIERACHY)
            		bw.write(from +";"+ to +";"+description+";(Peso Inválido para implementação)\n");
            	else
            		bw.write(from +";"+ to +";"+description+";(Falha ao aplicar peso)\n");
            }else {
            bw.write(from +";"+ to +";"+description+";"+weight+"\n");
            }
            // Fecha o BufferedWriter para liberar o recurso
            
            System.out.println("[Log] >> Texto escrito no arquivo com sucesso!");
            
            	aresta.get(from).add(to);
            	
        } catch (IOException e) {
            System.err.println("[Erro] >> Ocorreu um erro ao escrever no arquivo: " + e.getMessage());
        }
	}
	
    public static void analyzeClass(String className) throws IOException {
        ClassReader classReader = new ClassReader(className);
        classReader.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                name = name.replace('/', '.');
            	System.out.println("[Log] >> Class: " + name);
                if (superName != null) {
                	superName = superName.replace('/', '.');
                    System.out.println("[Log] >> Extends: " + superName);
                    addAresta(superName, name,instantionTime(superName), EXTEND_HIERACHY);
                }
                for (String iface : interfaces) {
                	String ifaceModified = iface.replace('/', '.');
                    System.out.println("[Log] >> Implements: " + ifaceModified);
                    
                    addAresta(ifaceModified, name,-1, IMPLEMENT_HIERACHY);
                    try {
                    	System.out.println("[Log] >> Adicionando Implement");
                    	analyzeClass(iface);
                    } catch (IOException e) {
                        System.err.println("[Alert] >> Erro ao analisar " + className + ": " + e.getMessage());
                    }
                }
            }

            /*@Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                System.out.println("  Method: " + name + " " + descriptor);
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }*/
        }, 0);
    }
    
    public static long instantionTime(String className) {
        // Obtenha a classe pelo nome
        long instantiationTime;
		try {
			Class<?> clazz = Class.forName(className);
	        long startTime = System.nanoTime();
			Object timed = clazz.newInstance();
	        long finalTime = System.nanoTime();
	        
	        instantiationTime = finalTime - startTime;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
        
		return instantiationTime;
        
    }
    
    public static void directoryToAnalyze(String javaLibPath) {
        File dir = new File(javaLibPath);
        if(!dir.exists()) { 
        	System.out.println("Diretório não encontrado!");
        	return; 
        }
        File[] arquivosArray = dir.listFiles();
        List<File> arquivosLista = Arrays.asList(arquivosArray);
        // Filtrar arquivos .class
        FilenameFilter classFilter = (d, name) -> name.endsWith(".class");
        
        // Listar arquivos .class
        String[] classFiles = dir.list(classFilter);
        if (classFiles != null) {
            for (String classFile : classFiles) {
                
                String className = classFile.substring(0, classFile.length() - 6); // Remove .class
                System.out.println("[Log] >> Encontrado: " + className);
                
                if(className.indexOf('$') != -1) {
                	className = className.substring(0, className.indexOf('$'));
                }
                
                if(arquivosLista.contains(className)) continue;
                
                String name = javaLibPath.substring(javaLibPath.indexOf("rt") + 3);
                System.out.println(name + className);
                name = name.replace('\\', '/');
                
                try {
                	System.out.println(javaLibPath);
                	analyzeClass(name + "/" + className);
                    System.out.println();
                } catch (IOException e) {
                    System.err.println("[Alert] >> Erro ao analisar " + className + ": " + e.getMessage());
                    if(name.lastIndexOf('/') != -1) {
                    	addAresta(name.substring(name.lastIndexOf('/') + 1), className, instantionTime((name + "/" + className).replace('/','.')), ARCHIVE_HIERACHY + " (Erro de Analise)");
                    } else {
                    	addAresta(name, className, instantionTime((name + "/" + className).replace('/','.')), ARCHIVE_HIERACHY + " (Erro de Analise)");
                    }
                }
            }
        }
        
    	for (File file : dir.listFiles()) {
            if(file.isDirectory()) {
            	
        		System.out.println(javaLibPath);
            	if(javaLibPath.indexOf('\\') != -1) {
            	int lastCaseString = javaLibPath.lastIndexOf('\\');
            	int penultCaseString = javaLibPath.lastIndexOf('\\', lastCaseString - 1);
            	
            	String from = javaLibPath.substring(penultCaseString + 1, lastCaseString);
            	String to = javaLibPath.substring(lastCaseString + 1);
            	
	            addAresta(from, to, -1, DIRECTORY_HIERACHY);
	            	
            	}
            	
            	directoryToAnalyze(file.getAbsolutePath());
            	}
        }
    }

    public void generateDataSet() {
    	directoryToAnalyze("C:\\Users\\maisg\\eclipse-workspace\\iesb_pg2024_project\\src\\main\\java\\iesb\\pg2024\\project\\util\\rt");
        // Exemplo de classes para analisar
        try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
