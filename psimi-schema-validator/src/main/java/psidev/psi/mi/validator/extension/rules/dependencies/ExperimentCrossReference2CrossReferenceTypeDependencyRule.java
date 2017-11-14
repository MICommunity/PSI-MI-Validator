package psidev.psi.mi.validator.extension.rules.dependencies;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.jami.model.Experiment;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.validator.extension.MiContext;
import psidev.psi.mi.validator.extension.MiValidatorContext;
import psidev.psi.mi.validator.extension.rules.MiExperimentRule;
import psidev.psi.mi.validator.extension.rules.RuleUtils;
import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.ontology_manager.interfaces.OntologyAccess;
import psidev.psi.tools.validator.ValidatorException;
import psidev.psi.tools.validator.ValidatorMessage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Rule that allows to check whether the experiment cross reference specified matches the cross-reference type.
 *
 * Rule Id = 15.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>26/03/13</pre>
 */

public class ExperimentCrossReference2CrossReferenceTypeDependencyRule extends MiExperimentRule {

    private static final Log log = LogFactory.getLog(ExperimentCrossReference2CrossReferenceTypeDependencyRule.class);

    /**
     * The dependency mapping object for this rule
     */
    private DependencyMapping mapping;

    //////////////////
    // Constructors

    /**
     *
     * @param ontologyManager
     */
    public ExperimentCrossReference2CrossReferenceTypeDependencyRule( OntologyManager ontologyManager ) {
        super( ontologyManager );
        MiValidatorContext validatorContext = MiValidatorContext.getCurrentInstance();

        OntologyAccess mi = ontologyManager.getOntologyAccess( "MI" );
        String fileName = validatorContext.getValidatorConfig().getExperimentCrossReference2CrossReferenceType();

        try {

            URL resource = ExperimentCrossReference2CrossReferenceTypeDependencyRule.class
                    .getResource( fileName );

            // Create a new instance of dependency mapping
            mapping = new DependencyMapping();
            // Build the dependency mapping from the file
            mapping.buildMappingFromFile( mi, resource );

        } catch (IOException e) {
            throw new ValidatorRuleException("We can't build the map containing the dependencies from the file " + fileName, e);
        } catch (ValidatorException e) {
            throw new ValidatorRuleException("We can't build the map containing the dependencies from the file " + fileName, e);
        }
        // describe the rule.
        setName( "Dependency Check : Experiment Cross reference database and cross reference qualifier" );
        setDescription( "Checks that each association database - qualifier respects IMEx curetion rules. For example, for each feature, all the interpro" +
                " cross references should have a qualifier 'identity'.");
        addTip( "Search the possible terms for database cross reference and reference type on https://www.ebi.ac.uk/ols/ontologies/mi" );
        addTip( "Look at the file https://github.com/MICommunity/PSI-MI-Validator/blob/master/psimi-schema-validator/src/main/resources/experimentCrossReference2CrossRefType.tsv for the possible dependencies cross reference database - qualifier" );
    }

    ///////////////////////
    // ObjectRule

    /**
     * For each cross reference of this XrefContainer, collects all respective cross reference type(s) and
     * check if the dependencies are correct.
     *
     * @param container to check on.
     * @return a collection of validator messages.
     *         if we fail to retreive the MI Ontology.
     */
    public Collection<ValidatorMessage> check( Experiment container) throws ValidatorException {

        Collection<ValidatorMessage> messages = Collections.EMPTY_LIST;

        // Collect the db references
        Collection<Xref> databaseReferences = container.getXrefs();
        if (!databaseReferences.isEmpty()){
            messages = new ArrayList<ValidatorMessage>();
            for ( Xref reference : databaseReferences) {
                MiContext context = RuleUtils.buildContext(reference, "database cross reference");
                context.addAssociatedContext(RuleUtils.buildContext(container, "experiment"));

                // build a context in case of error
                messages.addAll( mapping.check( reference.getDatabase(), reference.getQualifier(), context, this ) );
            }
        }

        return messages;
    }

    public String getId() {
        return "R56";
    }
}