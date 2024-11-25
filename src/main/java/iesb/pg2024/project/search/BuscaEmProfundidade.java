package iesb.pg2024.project.search;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

import iesb.pg2024.project.core.Grafo;
import iesb.pg2024.project.core.Vertice;

// Algoritmo em Grafo: Depth-First Search
public class BuscaEmProfundidade {

	private static BuscaEmProfundidade instance;
	
	private BuscaEmProfundidade() {}
	
	public static BuscaEmProfundidade getInstance() {
		if(instance == null) {
			instance = new BuscaEmProfundidade();
		}
		
		return instance;
	}
	
	public List<String> buscar(Grafo grafo, String origem, String destino){
		
		Stack<String> rastroPercorrido = new Stack<String>();
		LinkedHashSet<String> verticesVisitados = new LinkedHashSet<String>();
		
		Caminho caminho = new Caminho();
		
		// marcado o meu primeiro rastro como a origem
		rastroPercorrido.push(origem);
		
		while(!rastroPercorrido.empty()) {
			String rastro = rastroPercorrido.pop();
			
			if(rastro.equals(destino)) {
				break;
			}
			
			for(Vertice v : grafo.getGrafoAdjacencias(rastro)) {
				String rotulo = v.getRotulo();
		
				if(!verticesVisitados.contains(rotulo)) {
					verticesVisitados.add(rotulo);
					caminho.ligarVertices(rotulo, rastro);
					rastroPercorrido.push(rotulo);
				}			
			}
		}	
		
		return caminho.gerarCaminho(origem, destino);
	}	
}