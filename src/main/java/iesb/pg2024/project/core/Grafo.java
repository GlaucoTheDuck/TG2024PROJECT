package iesb.pg2024.project.core;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grafo {

	private int qtdMaximaVertices;
	private int qtdAtualVertices = 0;
	
	// G = (V, E)
	private List<Vertice> vertices = new ArrayList<Vertice>();
	private Map<String, Integer> rotulosEmIndices = new HashMap<String, Integer>();
	
	private MatrizAdjacencia matrizAdjacencia;
	
	//@novo
	public MatrizAdjacencia getMatrizAdjacencia() {
		return this.matrizAdjacencia;
	}
	
	// metodo construtor
	public Grafo(int qtdMaximaVertices) {
		this.qtdMaximaVertices = qtdMaximaVertices;
	}
	
	public void adicionarVertices(String rotulo) throws Exception {
		if(this.qtdAtualVertices <= this.qtdMaximaVertices - 1) {
			
			Vertice vertice = new Vertice(rotulo);
			this.vertices.add(vertice);
			this.rotulosEmIndices.put(rotulo, this.qtdAtualVertices);
			this.qtdAtualVertices++;			
			
		} else {
			throw new Exception("A quantidade de vértices permitida (" +
					this.qtdMaximaVertices + ") foi excedida!");
		}
	}
	
	public List<Vertice> getVertices(){
		return this.vertices;
	}
	
	public void conectarVertices(String rotuloVerticeInicial,
			String rotuloVerticeFinal, int peso) throws Exception {
		
		if(!isExisteVertice(rotuloVerticeInicial) || !isExisteVertice(rotuloVerticeFinal)) {
			throw new 
				Exception("Para adicionar uma aresta ambos os vértices devem existir!");
		}
		
		// Matriz Adjacencia criada e inicializada
		this.criarMatrizAdjacencia();		
		int indiceVerticeFinal = this.rotulosEmIndices.get(rotuloVerticeInicial);
		int indiceVerticeInicial = this.rotulosEmIndices.get(rotuloVerticeFinal);
		
		// adicao de novas arestas
		this.matrizAdjacencia.adicionarAresta(indiceVerticeInicial, indiceVerticeFinal, peso);				
	}
	
	private void criarMatrizAdjacencia() {
		if(this.matrizAdjacencia == null) {
			this.matrizAdjacencia = 
					new MatrizAdjacencia(new ArrayList<Vertice>(this.vertices));
		}
	}
	
	private boolean isExisteVertice(String rotuloVertice) {		
		int indice = this.rotulosEmIndices.get(rotuloVertice);
		boolean isVerticeAusente = this.vertices.get(indice) != null ? true : false;
		
		return isVerticeAusente;
	}
	
	public Vertice getVertice(String rotulo) {
		this.isExisteVertice(rotulo);
		int indice = this.rotulosEmIndices.get(rotulo);
		return this.vertices.get(indice);
	}
	
	public List<Vertice> getGrafoAdjacencias(String vertice){
		// preciso fazer a verificacao se o vertice existe
		int indiceVertice = this.rotulosEmIndices.get(vertice);
		
		return this.matrizAdjacencia.getAdjacencias(indiceVertice);
	}
	
	// @novo
	public void imprimirMatrizAdjacenciaGrafo() {
		int lengthMatriz = this.matrizAdjacencia.getMatriz().length;
		
		for (int i = 0; i < lengthMatriz; i++) {
			for (int j = 0; j < lengthMatriz; j++) {
				System.out.print(this.matrizAdjacencia.getMatriz()[i][j] + " ");
			}
			System.out.println();
		}		
	}
	
	public void imprimirVertices() {
		for (Vertice vertice : vertices) {
			System.out.println(vertice.getRotulo());
		}
	}
	
	public int getWeight(String verticeInicial, String verticeFinal){
		int linha = this.rotulosEmIndices.get(verticeInicial);
		int coluna = this.rotulosEmIndices.get(verticeFinal);
		return this.matrizAdjacencia.getWeight(linha, coluna);
	}
}