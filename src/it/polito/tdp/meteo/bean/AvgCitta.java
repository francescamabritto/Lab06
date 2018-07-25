package it.polito.tdp.meteo.bean;

public class AvgCitta {
	
	private String nomeCitta;
	private Double avgUmidita;
	
	
	public AvgCitta(String nomeCitta, Double avgUmidita) {
		this.nomeCitta = nomeCitta;
		this.avgUmidita = avgUmidita;
	}
	
	public String getNomeCitta() {
		return nomeCitta;
	}
	public void setNomeCitta(String nomeCitta) {
		this.nomeCitta = nomeCitta;
	}
	public Double getAvgUmidita() {
		return avgUmidita;
	}
	public void setAvgUmidita(Double avgUmidita) {
		this.avgUmidita = avgUmidita;
	}

	@Override
	public String toString() {
		return  nomeCitta + "  ->  " + avgUmidita ;
	}
	
	
	
}
