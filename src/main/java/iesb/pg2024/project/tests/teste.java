package iesb.pg2024.project.tests;

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

public class teste {	
	public static final String ARCHIVE_HIERACHY = "Hierarquia de Arquivo";
	
	public static Map<String, List<String>> directoryHierachy = new Hashtable<>();
	
	public static void addAresta(String from, String to, String description) {
		
        File arquivo = new File("C:\\Users\\maisg\\OneDrive\\Área de Trabalho\\AI\\arestas.txt");
        
        try {
            // Cria FileWriter com modo de "append" (verdadeiro para adicionar ao final do arquivo, falso para sobrescrever)
            FileWriter fw = new FileWriter(arquivo, true);
            BufferedWriter bw = new BufferedWriter(fw);

            // Escreve o texto
            bw.write(from +";"+ to +";"+description+"\n");
            
            // Fecha o BufferedWriter para liberar o recurso
            bw.close();
            System.out.println("Texto escrito no arquivo com sucesso!");
            if(description == ARCHIVE_HIERACHY) {
            	directoryHierachy.get(from).add(to);
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao escrever no arquivo: " + e.getMessage());
        }
	}
	
    public static void analyzeClass(String className) throws IOException {
        ClassReader classReader = new ClassReader(className);
        classReader.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                name = name.replace('/', '.');
            	System.out.println("Class: " + name);
                if (superName != null) {
                	superName = superName.replace('/', '.');
                    System.out.println("  Extends: " + superName);
                    addAresta(superName, name, "Exentend");
                }
                for (String iface : interfaces) {
                	iface = iface.replace('/', '.');
                    System.out.println("  Implements: " + iface);
                    addAresta(iface, name, "Implement");
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
                System.out.println("Encontrado: " + className);
                
                if(className.indexOf('$') != -1) {
                	className = className.substring(0, className.indexOf('$'));
                }
                
                if(arquivosLista.contains(className)) continue;
                
                try {
                	System.out.println(javaLibPath);
                	String name = javaLibPath.substring(javaLibPath.indexOf("java"));
                    System.out.println(name + className);
                    name = name.replace('\\', '/');
                	analyzeClass(name + "/" + className);
                    System.out.println();
                } catch (IOException e) {
                    System.err.println("Erro ao analisar " + className + ": " + e.getMessage());
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
            	if(!directoryHierachy.containsKey(from)) {
            		directoryHierachy.put(from, new ArrayList<>());
            	}
            	if(directoryHierachy.containsKey(from) && !directoryHierachy.get(from).contains(to)) 
            		{
	            		addAresta(from, to, ARCHIVE_HIERACHY);
	            	}
            	}
            	
            	directoryToAnalyze(file.getAbsolutePath());
            	}
        }
    }

    public static void main(String[] args) {
    	directoryToAnalyze("C:\\Program Files (x86)\\Java\\jre1.8.0_231\\lib\\rt\\java");
        // Exemplo de classes para analisar
        
    }
}
