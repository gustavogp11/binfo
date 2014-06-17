package com.bioinfo.tp.ej2;

import static org.biojava3.ws.alignment.qblast.BlastAlignmentParameterEnum.ENTREZ_QUERY;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.util.IOUtils;
import org.biojava3.ws.alignment.qblast.*;

/**
 *
 * @author Nicolas
 */
public class Blast {    

	/**
     * 
     * @param genbankFile: archivo en formato genbank
     * @param outputFile: nombre del archivo de salida
     * @return Secuencia de aminoacidos en formato FASTA
     * @throws Exception 
     */     
    public static void doBlast(InputStream fastaFile, String outputFile) throws Exception
    {
        NCBIQBlastService service = new NCBIQBlastService();

        //set alignment options
        NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
        props.setBlastProgram(BlastProgramEnum.blastp);
        props.setBlastDatabase("swissprot");
        props.setAlignmentOption(ENTREZ_QUERY, "\"serum albumin\"[Protein name] AND mammals[Organism]");

        //set output options
        NCBIQBlastOutputProperties outputProperties = new NCBIQBlastOutputProperties();
        outputProperties.setOutputFormat(BlastOutputFormatEnum.HTML);

        String requestId = null;
        FileWriter writer = null;
        BufferedReader reader = null;

        //get protein seq from file
        HashMap<String,ProteinSequence> seq = FastaReaderHelper.readFastaProteinSequence(fastaFile);
        String sequence = "";
        for (Map.Entry<String, ProteinSequence> sequenceEntry : seq.entrySet()) {
            sequence = sequenceEntry.getValue().getSequenceAsString();
        }
        System.out.println("Seq is: " + sequence);


        try{
            requestId = service.sendAlignmentRequest(sequence, props);

            //wait for the results
            while(!service.isReady(requestId)){
                System.out.println("Waiting for results...");
                Thread.sleep(5000);
            }

            InputStream in = service.getAlignmentResults(requestId, outputProperties);
            reader = new BufferedReader(new InputStreamReader(in));

            File f = new File(outputFile);
            System.out.println("Saving results");
            writer = new FileWriter(f);

            String line;
            while ((line = reader.readLine()) != null){
                writer.write(line + System.getProperty("line.separator"));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally {
            IOUtils.close(writer);
            IOUtils.close(reader);
            service.sendDeleteRequest(requestId);
        }


    }
    
    private static void checkArgs(String[] args) {
        if(args == null || (args.length < 2))
            throw new RuntimeException("Debe ingresar 1- ruta y nombre completo del archivo input en formato Fasta. "
                    + "2- ruta y nombre completo del archivo output (blast)");
    }
    
    public static void main(String[] args) throws Exception {
        try {
            args = new String[] { "/Users/nicolaslaplume/Documents/utn/bioinfo/binfo/tp/src/main/resources/ej.fasta" ,
                    "/Users/nicolaslaplume/Documents/utn/bioinfo/binfo/tp/src/main/resources/output.bst"};
            checkArgs(args);
            InputStream input = new FileInputStream(args[0].toString());
            String outputFile = args[1].toString();



            doBlast(input, outputFile);
        } catch(Exception e) {
            System.out.println("Error (" + e + "): " + e.getMessage());
        }
    }
    
    
    
}
