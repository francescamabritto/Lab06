package it.polito.tdp.meteo;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		
//		System.out.format("media rilevamenti = %.2f",m.getAvgRilevamentiLocalitaMese(1, "Genova"));
		
		System.out.println("umidità media per citta:\n" + m.getUmiditaMedia(12));
		
		m.trovaSequenza(1);
		
		System.out.println(m.trovaSequenza(5));
		
//		System.out.println(m.trovaSequenza(4));
	}

}
