package modele;

import java.util.ArrayList;

import exception.DejaDansFamilleException;

public abstract class Famille {
	protected String numFamille;

	protected ArrayList<Personne> listeDesEnfants = new ArrayList<Personne>();

	protected Personne parent_1;

	protected Personne parent_2;

	/**
	 * Constructeur de la classe Famille
	 * 
	 * @param numFamille numero unique permettant d'identifier la Famille
	 */
	public Famille(String numFamille) {
		this.numFamille = numFamille;
	}

	/**
	 * methode permettant d'obtenir le numero identifiant la Famille
	 * 
	 * @return numero de la Famille
	 */
	public String getNumFamille() {
		return numFamille;
	}

	/**
	 * methode permettant d'obtenir la liste des enfants de la Famille
	 * 
	 * @return liste des enfants de la Famille
	 */
	public ArrayList<Personne> getListeDesEnfants() {
		return listeDesEnfants;
	}

	/**
	 * methode permettant d'obtenir l'un des deux parents de la Famille(instance de
	 * la classe Personne)
	 * 
	 * @return Un des Parents de la Famille
	 */
	public Personne getParent_1() {
		return parent_1;
	}

	/**
	 * methode permettant de modifier l'un des deux Parents de la Famille (instance
	 * de la classe Personne)
	 * 
	 * @param parent_1 la personne a mettre comme Parent
	 * @throws DejaDansFamilleException Exception signalant que la Personne est deja
	 *                                  un membre de la Famille(Parent ou Enfant)
	 */
	public void setParent_1(Personne parent_1) throws DejaDansFamilleException {
		if (estDejaDansFamille(parent_1)) {
			throw new DejaDansFamilleException("La personne est déjà un membre de"
					+ " la famille, elle ne peut pas être ajoutée en tant que Parent_1");
		}
		this.parent_1 = parent_1;
	}

	/**
	 * methode permettant d'obtenir l'un des deux parents (instance de la classe
	 * Personne)
	 * 
	 * @return Un des Parents de la Famille
	 */
	public Personne getParent_2() {
		return parent_2;
	}

	/**
	 * methode permettant de modifier l'un des deux Parents de la Famille (instance
	 * de la classe Personne)
	 * 
	 * @param parent_2 la personne a mettre comme Parent
	 * @throws DejaDansFamilleException Exception signalant que la Personne est deja
	 *                                  un membre de la Famille(Parent ou Enfant)
	 */
	public void setParent_2(Personne parent_2) throws DejaDansFamilleException {
		if (estDejaDansFamille(parent_2)) {
			throw new DejaDansFamilleException("La personne est déjà un membre de"
					+ " la famille, elle ne peut pas être ajoutée en tant que Parent_1");
		}
		this.parent_2 = parent_2;
	}

	/**
	 * methode hashCode() generee par Eclipse
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numFamille == null) ? 0 : numFamille.hashCode());
		return result;
	}

	/**
	 * retourne true si le numero de Famille(instance courante) est identique à
	 * celui de l'objet passe en parametre. retourne false si l'objet passe en
	 * parametre est null, d'une classe differente de celle de l'instance courante
	 * ou si le numero de Famille est different de celui de l'instance courante
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Famille famille = (Famille) obj;
		if (numFamille == null) {
			if (famille.numFamille != null)
				return false;
		} else if (!numFamille.equals(famille.numFamille))
			return false;
		return true;
	}

	/**
	 * retourne le toString() des Parents et Enfants de la Famille (instances de la
	 * classe Personne)
	 */
	@Override
	public String toString() {
		String famille = "";

		famille += parent_1.toString() + "\n";
		famille += parent_2.toString() + "\n";

		for (Personne enfant : listeDesEnfants) {
			famille += enfant.toString() + "\n";
		}
		return famille;
	}

	/**
	 * methode permettant d'ajouter un Enfant dans la liste des Enfants de la
	 * Famille
	 * 
	 * @param enfant Personne a ajouter en tant qu'enfant
	 * @throws DejaDansFamilleException Exception signalant que la Personne passee
	 *                                  en parametre est deja dans la Famille(
	 *                                  Enfant ou Parent )
	 */
	public void ajouterEnfant(Personne enfant) throws DejaDansFamilleException {
		if (estDejaDansFamille(enfant)) {
			throw new DejaDansFamilleException("La personne est déjà un membre "
					+ "de la famille, elle ne peut pas être ajoutée en tant qu'enfant");
		}
		listeDesEnfants.add(enfant);
	}

	/**
	 * methode permettant de verifier si une Personne est dans la Famille
	 * 
	 * @param personne Membre potentiel
	 * @return true si la Personne passee en parametre est soit un Enfant, soit un
	 *         des deux Parents de la Famille. false dans l'autre cas
	 */
	public boolean estDejaDansFamille(Personne personne) {
		if (estDansListeDesEnfant(personne) || estUnParentDeLaFamille(personne)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * methode permettant de verifier si une Personne est un des Enfants de la
	 * Famille
	 * 
	 * @param personne Enfant potentiel
	 * @return true si la Personne passee en parametre est un des Enfants de la
	 *         Famille. false dans l'autre cas
	 */
	public boolean estDansListeDesEnfant(Personne personne) {
		if (listeDesEnfants.contains(personne)) {
			return true;
		}
		return false;
	}

	/**
	 * methode permettant de verifier si une Personne est un des Parents de la
	 * Famille
	 * 
	 * @param personne Parent potentiel
	 * @return true si la Personne passee en parametre est un des deux Parents de la
	 *         Famille. false dans l'autre cas
	 */
	public boolean estUnParentDeLaFamille(Personne personne) {
		if (parent_1.equals(personne) || parent_2.equals(personne)) {
			return true;
		}
		return false;
	}

}
