package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.Date;
import java.time.*;
import java.util.List;


import it.polito.tdp.meteo.bean.AvgCitta;
import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO dao;
	private List<Citta> allCitta;
	private List<SimpleCity> allSimpleCity;
	private List<SimpleCity> sequenzaOttima; 
	
	public Model() {
		this.dao = new MeteoDAO();
		this.allCitta = dao.getAllCitta();
		this.allSimpleCity = new ArrayList<>();
		popolaSimpleCity(allCitta);
		this.sequenzaOttima = new ArrayList<>();
	}
	
	public void popolaSimpleCity(List<Citta>citta){
		for(Citta c: citta) {
			SimpleCity sc = new SimpleCity(c.getNome());
			allSimpleCity.add(sc);
		}
	}
	

	public List<AvgCitta> getUmiditaMedia(int mese) {
		
		List<AvgCitta> avgUmiditaCitta = new ArrayList<>();
		
		for(Citta c: allCitta) {
			AvgCitta ac = new AvgCitta (c.getNome(), this.getAvgRilevamentiLocalitaMese(mese, c.getNome()));
			avgUmiditaCitta.add(ac);
		}
			
		return avgUmiditaCitta;
	}
	
	
	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		Double sum=0.0;
		int n=0;
		List<Rilevamento> ril = dao.getAllRilevamentiLocalitaMese(mese, localita);
		for(Rilevamento r: ril) {
			sum = r.getUmidita() + sum ;
			n++;
		}
		Double avg = (sum/n);
		return avg;
	}

	/**
	 * date 3 citta , 
	 * analisi tecniche della durata di un giorno in ciascuna citta. 
	 * Costo delle analisi:
	 * - fattore costante (di valore 100) quando il tecnico si deve spostare da una città ad un’altra 
	 * in due giorni successivi, 
	 * - fattore variabile pari al valore numerico dell’umidità della città nel giorno considerato. 
 
Si trovi la sequenza delle città da visitare nei primi 15 giorni del mese selezionato, 
tale da minimizzare il costo complessivo rispettando i seguenti vincoli:

- Nei primi 15 giorni del mese, tutte le città devono essere visitate almeno una volta
- In nessuna città si possono trascorrere più di 6 giornate (anche non consecutive)
- Scelta una città, il tecnico non si può spostare prima di aver trascorso 3 giorni consecutivi.
	 * */

	
	
	
	public String trovaSequenza(int mese) {
		
		recursive(new ArrayList<SimpleCity>(), 0);
		
		return this.sequenzaOttima.toString();
	}
	
	private void recursive(List<SimpleCity> parziale, int giorno) {
		
		Date data = new Date();
		
		Double punteggioOttimo = 0.0;
		
		if(giorno >= NUMERO_GIORNI_TOTALI) {
			
			if(this.controllaParziale(parziale)==true) {
				if(this.punteggioSoluzione(parziale)>0) {
					this.sequenzaOttima = new ArrayList<>(parziale);
					punteggioOttimo = this.punteggioSoluzione(parziale);
				}
				System.out.println(parziale.toString());
			}
			return;
		}
		
		List<SimpleCity> copiaAllSimpleCity = new ArrayList<>(allSimpleCity);
		for(SimpleCity s: copiaAllSimpleCity) {
			parziale.add(s);
			recursive(parziale, giorno+1);
			parziale.remove(s);
			
		}
	}
	

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		int spostamenti = 0;
		int costoCitta = 0;
		for(int i=0; i<14;i++) {
			if((soluzioneCandidata.get(i)).getNome().compareTo(soluzioneCandidata.get(i+1).getNome())!=0) {
				spostamenti++;
			}
			costoCitta += soluzioneCandidata.get(i).getCosto();
		}
		
		double score = COST*spostamenti + costoCitta;
		return score;
	}

	/** - Nei primi 15 giorni del mese, tutte le città devono essere visitate almeno una volta
	- In nessuna città si possono trascorrere più di 6 giornate (anche non consecutive)
	- Scelta una città, il tecnico non si può spostare prima di aver trascorso 3 giorni consecutivi.
	 */
	private boolean controllaParziale(List<SimpleCity> parziale) {
		
		//Tutte le citta visiate almeno una volta
		
		List<String> cittaVisitate = new ArrayList<>();
		for(SimpleCity sc: parziale) {
			if(!cittaVisitate.contains(sc.getNome())) {
				cittaVisitate.add(sc.getNome());
			}
		}
		if((cittaVisitate.size()!=3))
			return false;
		
		//In nessuna città si possono trascorrere più di 6 giornate (anche non consecutive)
		
		for(String s: cittaVisitate) {
			int count = 0;
			for(SimpleCity sc: parziale) {
				if(s.compareTo(sc.getNome())==0)
					count++;
			}
			if(count>6||count<3)
				return false;
			
		}
		
		//Scelta una città, il tecnico non si può spostare prima di aver trascorso 3 giorni consecutivi.
		int c = 0;	
		for(int i=0; i< 14;i++) {
			if((parziale.get(i)).getNome().compareTo(parziale.get(i+1).getNome())!=0) {
				if(c<3)
					return false;
				c = 0;
			}
			else
				c++;
		
		}
		return true;
	}
	
	
	

}
