package iesb.pg2024.project.app;

//import iesb.pg2024.project.dataset.datasetGenerate;

import iesb.pg2024.project.dataset.DatasetGenerator;

public class ProjectMain {

	public static void main(String[] args)
		throws ReflectiveOperationException{
		// TODO Auto-generated method stub
		DatasetGenerator datasetGenerator = new DatasetGenerator();
		datasetGenerator.generateDataSet();
		
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
