package modele;

import java.util.HashMap;
import java.util.LinkedList;

import modele.infos.Information;

public abstract class Personne implements Comparable<Personne> {
	protected final String numPersonne;

	protected HashMap<String, Information> listeDesInfos = new HashMap<String, Information>();

	/**
	 * constructeur de la classe PersonneConcrete
	 * 
	 * @param numPersonneConcrete numero unique permettant d'identifier la
	 *                            PersonneConcrete
	 */
	public Personne(String numPersonne) {
		super();
		this.numPersonne = numPersonne;
	}

	/**
	 * methode permettant d'obtenir le munero de la PersonneConcrete
	 * 
	 * @return numero de la PersonneConcrete
	 */
	public String getNum() {
		return numPersonne;
	}

	/**
	 * methode retournant la liste des informations d'une personne
	 * 
	 * @return liste des infos de la personne
	 */
	public HashMap<String, Information> getInformations() {
		return listeDesInfos;
	}

	/**
	 * methode permettant de recuperer une information designee avec une cle(ex: les
	 * surnoms d'une personne key = Surnoms)
	 * 
	 * @param key cle identifiant l'information recherchee
	 * @return information cherchee sous forme de liste de chaine de caracteres (ex:
	 *         pour les surnoms retourne chaque surnom dans une case de la liste)
	 */
	public LinkedList<String> getInformation(String key) {
		if (listeDesInfos.get(key) == null) {
			LinkedList<String> vide = new LinkedList<>();
			return vide;
		}
		return listeDesInfos.get(key).getListeInformation();
	}

	/**
	 * methode permettant d'ajouter une donnee a une information designee avec une
	 * cle
	 * 
	 * @param key  cle identifiant l'information
	 * @param info donnee a ajouter a l'information
	 */
	public abstract void ajouterInformation(String key, String info);

	/**
	 * Methode hashCode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numPersonne == null) ? 0 : numPersonne.hashCode());
		return result;
	}

	/**
	 * retourne true si le numero de PersonneConcrete(instance courante) est
	 * identique à celui de l'objet passe en parametre. retourne false si l'objet
	 * passe en parametre est null, d'une classe differente de celle de l'instance
	 * courante ou si le numero de PersonneConcrete est different de celui de
	 * l'instance courante
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Personne other = (Personne) obj;
		if (numPersonne == null) {
			if (other.numPersonne != null)
				return false;
		} else if (!numPersonne.equals(other.numPersonne))
			return false;
		return true;
	}

}
