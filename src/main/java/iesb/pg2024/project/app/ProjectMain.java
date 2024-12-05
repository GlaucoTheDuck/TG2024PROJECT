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
			String labels[];
			String subLabels[];
			
//			System.out.println(">> Counting nodes...");
			do {
				labels = br.readLine().split(";");

				subLabels = labels[0].split("\\.");
				vertices.add(subLabels[subLabels.length-1]);
				subLabels = labels[1].split("\\.");
				vertices.add(subLabels[subLabels.length-1]);
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
				subLabels = labels[0].split("\\.");
				String vertice1 = subLabels[subLabels.length-1];
				subLabels = labels[1].split("\\.");
				String vertice2 = subLabels[subLabels.length-1];
				if(m.matches()) {
					grafo.conectarVertices(vertice1, vertice2, Integer.parseInt(labels[3]));
//					System.out.println(">> " + labels[0] + " -> " + labels[1]);//test
//					System.out.println(">> Weight: " + labels[3]);//test
				}else {
					grafo.conectarVertices(vertice1, vertice2, 0);
				}
			} while ((line = br.readLine())!= null);
			br.close();
//			System.out.println(">> Edges added.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String origem = "rt";
		String destino = "UnsupportedOperationException";
		BuscaEmProfundidade buscaEmProfundidade = BuscaEmProfundidade.getInstance();
		List<String> caminho = buscaEmProfundidade.buscar(grafo, origem, destino);
		
		System.out.println("(" + caminho.get(0) + ")");
		int pesoUltimaAresta = grafo.getWeight(caminho.get(0), caminho.get(1));
		int pesoArestaAtual = 0;
		int pesoVertice = 0;
		for (int i = 1; i < caminho.size(); i++) {
			if(i != caminho.size()-1) pesoArestaAtual = grafo.getWeight(caminho.get(i), caminho.get(i+1));
			pesoVertice = pesoArestaAtual - pesoUltimaAresta;
			if(pesoVertice <= 0) {
				System.out.println("(" + caminho.get(i) + " - Peso: <100 nanosegundos)");
			}else {
				System.out.println("(" + caminho.get(i) + " - Peso: " + pesoVertice + " nanosegundos)");
			}
			pesoUltimaAresta = pesoArestaAtual;
		}
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
