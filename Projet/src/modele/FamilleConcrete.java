package modele;

public class FamilleConcrete extends Famille {

	public FamilleConcrete(String numFamille) {
		super(numFamille);
		parent_1 = new PersonneConcrete("inconnu");
		parent_2 = new PersonneConcrete("inconnu");
	}

}
