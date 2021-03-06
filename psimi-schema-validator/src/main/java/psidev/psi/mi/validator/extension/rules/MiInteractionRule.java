package psidev.psi.mi.validator.extension.rules;

import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.validator.extension.MiOntology;
import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.ontology_manager.interfaces.OntologyAccess;
import psidev.psi.tools.validator.ValidatorException;
import psidev.psi.tools.validator.ValidatorMessage;

import java.util.Collection;

/**
 * <b> PSI-MI 2.5 Specific Rule </b>.
 * <p/>
 * That class is only meant to be checking on Interactions. </p>
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 2.0.0
 */
public abstract class MiInteractionRule extends AbstractMIRule<Interaction> {

    //////////////////
    // Constructors

    public MiInteractionRule(OntologyManager ontologyManager) {
        super( ontologyManager, Interaction.class );
    }

    ///////////////////////
    // ObjectRule

    public abstract Collection<ValidatorMessage> check( Interaction interaction ) throws ValidatorException;

    ///////////////////////
    // Utility methods

    /**
     * Return the MI ontology
     *
     * @return the ontology
     * @throws ValidatorException
     */
    public OntologyAccess getMiOntology() throws ValidatorException {
        return ontologyManager.getOntologyAccess( "MI" );
    }

    /**
     * Return the MI ontology
     *
     * @return the ontology
     * @throws psidev.psi.tools.validator.ValidatorException
     */
    public MiOntology getMi25Ontology() throws ValidatorException {
        return new MiOntology( ontologyManager.getOntologyAccess( "MI" ) );
    }
}