package modele.infos;

import java.util.LinkedList;

public abstract class Information {
	protected LinkedList<String> listeInformation;

	/**
	 * methode permettant de recuperer l'information de la classe (ex:pour les
	 * surnoms renvoie chaque surnom dans une case de la liste)
	 * 
	 * @return information recuperee
	 */
	public LinkedList<String> getListeInformation() {
		return listeInformation;
	}

	/**
	 * methode permettant d'ajouter une donnee a l'information
	 * 
	 * @param information donnee a ajouter
	 */
	public void ajouterInformation(String information) {
		listeInformation.add(information);
	}
}
