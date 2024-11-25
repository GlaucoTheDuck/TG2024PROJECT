package iesb.pg2024.project.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Caminho {

	private Map<String, String> caminho;
	
	// construtor
	public Caminho() {
		this.caminho = new HashMap<>();
	}
	
	public void ligarVertices(String anterior, String proximo) {
		this.caminho.put(anterior, proximo);
	}
	
	public List<String> gerarCaminho(String origem, String destino){
		List<String> resultado = new ArrayList<String>();
		String noh = destino;
		
		while(noh != origem && this.caminho.containsKey(noh)) {
			resultado.add(noh);
			noh = this.caminho.get(noh);
		}
		
		resultado.add(noh);
		
		// inverter a ordem dos elementos da lista
		Collections.reverse(resultado);
		
		return resultado;
	}	
}