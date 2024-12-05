package iesb.pg2024.project.core;

import java.util.ArrayList;
import java.util.List;

public class MatrizAdjacencia {

	private int[][] matriz;
	private List<Vertice> vertices;
	private int qtdVertices;
	
	public int[][] getMatriz(){
		return matriz;
	}
	
	// construtor
	public MatrizAdjacencia(List<Vertice> vertices)	{
		this.vertices = vertices;
		this.qtdVertices = vertices.size();
		this.matriz = new int[this.qtdVertices][this.qtdVertices];
		
		// inicializar a minha matriz de adjacencia
		this.inicializarMatriz(-1);
	}
	
	private void inicializarMatriz(int valorInicial) {
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				this.matriz[i][j] = valorInicial;
			}
		}
	}
	
	public void adicionarAresta(int indiceVerticeInicial, int indiceVerticeFinal, int peso) {
		Vertice verticeInicial = this.vertices.get(indiceVerticeInicial);
		Vertice verticeFinal   = this.vertices.get(indiceVerticeFinal);
		
		if(verticeInicial == verticeFinal) {
			this.matriz[indiceVerticeInicial][indiceVerticeFinal] = peso;
			verticeInicial.adicionarGrau();
		} else {
			this.matriz[indiceVerticeInicial][indiceVerticeFinal] = peso;
			verticeInicial.adicionarGrau();
			
			this.matriz[indiceVerticeFinal][indiceVerticeInicial] = peso;
			verticeFinal.adicionarGrau();
		}		
	}
	
	public List<Vertice> getAdjacencias(int indiceVertice){

		int linha = indiceVertice;
		List<Vertice> adjacencias = new ArrayList<Vertice>();
		
		for (int j = 0; j < this.vertices.size(); j++) {
			if(this.matriz[linha][j] != -1) {
				Vertice vertice = this.vertices.get(j);
				adjacencias.add(vertice);
			}
		}
		
		return adjacencias;
	}	
	
	public int getWeight(int indiceVerticeInicial, int indiceVerticeFinal){
		return matriz[indiceVerticeInicial][indiceVerticeFinal];
	}
}