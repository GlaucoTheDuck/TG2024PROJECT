package iesb.pg2024.project.dataset;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EdgeCreator {	
	public static final String DIRECTORY_HIERACHY = "Hierarquia de Diretório";
	public static final String EXTEND_HIERACHY = "Extend";
	public static final String IMPLEMENT_HIERACHY = "Implement";
	public static final String ARCHIVE_HIERACHY = "Hierarquia em Arquivo";

	
	public static Map<String, List<String>> directoryHierachy = new Hashtable<>();
	public static Map<String, List<String>> aresta = new Hashtable<>();
	
	public static void addAresta(String from, String to, String description) {
		
		if(!aresta.containsKey(from)) {
        	aresta.put(from, new ArrayList<String>());
        }
        if(aresta.get(from).contains(to)) {
        	System.out.println("[Warning] >> Aresta já adicionada");
        	return;
        }
		
        File arquivo = new File("C:\\Users\\2222130009\\Downloads\\rt\\arestas.txt");
        
        try {
            // Cria FileWriter com modo de "append" (verdadeiro para adicionar ao final do arquivo, falso para sobrescrever)
            FileWriter fw = new FileWriter(arquivo, true);
            BufferedWriter bw = new BufferedWriter(fw);

            // Escreve o texto
            bw.write(from +";"+ to +";"+description+"\n");
            
            // Fecha o BufferedWriter para liberar o recurso
            bw.close();
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
                    addAresta(superName, name, EXTEND_HIERACHY);
                }
                for (String iface : interfaces) {
                	String ifaceModified = iface.replace('/', '.');
                    System.out.println("[Log] >> Implements: " + ifaceModified);
                    
                    addAresta(ifaceModified, name, IMPLEMENT_HIERACHY);
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
    
    public static void directoryToAnalyze(String javaLibPath) {
        File dir = new File(javaLibPath);
        if(!dir.exists()) return; 
        
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
                    	addAresta(name.substring(name.lastIndexOf('/') + 1), className, ARCHIVE_HIERACHY + " (Erro de Analise)");
                    } else {
                    	addAresta(name, className, ARCHIVE_HIERACHY + " (Erro de Analise)");
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
            	
	            addAresta(from, to, DIRECTORY_HIERACHY);
	            	
            	}
            	
            	directoryToAnalyze(file.getAbsolutePath());
            	}
        }
    }

}
