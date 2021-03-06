package org.grobid.service.command;

import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.grobid.core.engines.Engine;
import org.grobid.core.engines.QuantitiesEngine;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.main.LibraryLoader;
import org.grobid.core.utilities.GrobidProperties;
import org.grobid.service.configuration.GrobidQuantitiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

public class UnitBatchProcessingCommand extends ConfiguredCommand<GrobidQuantitiesConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitBatchProcessingCommand.class);
    private final static String INPUT_DIRECTORY = "Input directory";
    private final static String OUTPUT_DIRECTORY = "Output directory";
    private final static String RECURSIVE = "recursive";


    public UnitBatchProcessingCommand() {
        super("batchUnits", "Process units files in batch");
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);

        subparser.addArgument("-dIn")
                .dest(INPUT_DIRECTORY)
                .type(String.class)
                .required(true)
                .help("Input directory");

        subparser.addArgument("-dOut")
                .dest(OUTPUT_DIRECTORY)
                .type(String.class)
                .required(true)
                .help("Output directory");

        subparser.addArgument("-r")
                .dest(RECURSIVE)
                .type(Boolean.class)
                .setDefault(false)
                .required(false)
                .help("Recursive processing");

    }

    @Override
    protected void run(Bootstrap bootstrap, Namespace namespace, GrobidQuantitiesConfiguration configuration) throws Exception {
        try {

            GrobidProperties.set_GROBID_HOME_PATH(new File(configuration.getGrobidHome()).getAbsolutePath());
            String grobidHome = configuration.getGrobidHome();
            if (grobidHome != null) {
                GrobidProperties.setGrobidPropertiesPath(new File(grobidHome, "/config/grobid.properties").getAbsolutePath());
            }

            GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(configuration.getGrobidHome()));
            GrobidProperties.getInstance(grobidHomeFinder);
            Engine.getEngine(true);
            LibraryLoader.load();
        } catch (final Exception exp) {
            System.err.println("Grobid initialisation failed, cannot find Grobid Home. Please use the option -gH to specify in the command.");
            exp.printStackTrace();

            System.exit(-1);
        }

        String inputDirectory = namespace.get(INPUT_DIRECTORY);
        String outputDirectory = namespace.get(OUTPUT_DIRECTORY);
        boolean isRecursive = namespace.get(RECURSIVE);

        new QuantitiesEngine().unitBatchProcess(inputDirectory, outputDirectory, isRecursive);

    }
}