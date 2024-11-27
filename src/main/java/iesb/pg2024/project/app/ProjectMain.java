package iesb.pg2024.project.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import iesb.pg2024.project.core.Grafo;
import iesb.pg2024.project.core.Vertice;

//import iesb.pg2024.project.dataset.datasetGenerate;

import iesb.pg2024.project.dataset.DatasetGenerator;
import iesb.pg2024.project.search.BuscaEmProfundidade;
import iesb.pg2024.project.search.Caminho;

public class ProjectMain {
	
		
	public static void main(String[] args)
		throws ReflectiveOperationException{
		// TODO Auto-generated method stub
//		DatasetGenerator datasetGenerator = new DatasetGenerator();
//		datasetGenerator.generateDataSet();
		
		Grafo grafo = null;
		try {
			File arestas = new File("C:\\Users\\Guilherme P. Mundim\\TG2024PROJECT\\src\\main\\java\\iesb\\pg2024\\project\\dataset\\arestas.txt");
			BufferedReader br = new BufferedReader(new FileReader(arestas));
			Set<String> vertices = new HashSet<String>();
			String line;
			String labels[] = new String[4];
			
//			System.out.println(">> Counting nodes...");
			do {
				labels = br.readLine().split(";");
				vertices.add(labels[0]);
				vertices.add(labels[1]);
			} while ((line = br.readLine())!= null);
//			System.out.println(">> Quantity of Nodes: [" + vertices.size() + "]");
			
//			System.out.println(">> Adding Nodes...");
			grafo = new Grafo(vertices.size());
			for (String string : vertices) {
				grafo.adicionarVertices(string);
			}
//			System.out.println(">> Nodes added.");
			br.close();
			
//			System.out.println(">> Adding edges...");
			br = new BufferedReader(new FileReader(arestas));
			Pattern p = Pattern.compile("[0-9]+");
			Matcher m;
			do {
				labels = br.readLine().split(";");
				m = p.matcher(labels[3]);
				if(m.matches()) {
					grafo.conectarVertices(labels[0], labels[1], Integer.parseInt(labels[3]));
//					System.out.println(">> " + labels[0] + " -> " + labels[1]);//test
//					System.out.println(">> Weight: " + labels[3]);//test
				}else {
					grafo.conectarVertices(labels[0], labels[1], 1);
				}
			} while ((line = br.readLine())!= null);
			br.close();
//			System.out.println(">> Edges added.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String origem = "java.lang.UnsupportedOperationException";
		String destino = "java.lang.Object";
		BuscaEmProfundidade buscaEmProfundidade = BuscaEmProfundidade.getInstance();
		List<String> caminho = buscaEmProfundidade.buscar(grafo, origem, destino);
		int weight = 0;
		
		for (int i = 0; i < caminho.size(); i++) {
			System.out.print(" -> " + caminho.get(i));
			if(i+1 != caminho.size()) {
				weight += grafo.getWeight(caminho.get(i), caminho.get(i+1));
			}
		}
		System.out.println();
		System.out.println(">> Tempo: " + weight + " nanosegundos.");
	}
	/*String className = "java.lang.instrument";
            // Obtenha a classe pelo nome
            Class<?> clazz = Class.forName(className);
            
            // Medição do tempo de criação da instância (incluindo as chamadas das superclasses)
            
            
            // Obtém o construtor padrão e instancia a classe
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            System.out.println("1");
            constructor.setAccessible(true);
            long startTime = System.nanoTime();
            Object instance = constructor.newInstance();
            long finalTime = System.nanoTime();
            
            long instantiationTime = finalTime - startTime;
            System.out.println("1");*/
}
