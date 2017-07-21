package org.metadatacenter.submission;

import biosample.TypeAttribute;
import biosample.TypeBioSample;
import biosample.TypeBioSampleIdentifier;
import common.sp.TypeDescriptor;
import common.sp.TypeOrganism;
import common.sp.TypePrimaryId;
import common.sp.TypeRefId;
import generated.TypeContactInfo;
import generated.TypeFileAttribute;
import generated.TypeName;
import generated.TypeOrganization;
import generated.TypeDepartment;
import generated.TypeSubmission;
import org.metadatacenter.submission.biosample.AIRRTemplate;
import org.metadatacenter.submission.biosample.OptionalBioSampleAttribute;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringWriter;
import java.util.GregorianCalendar;

/**
 * Convert a CEDAR JSON Schema-based AIRR template instance into a BioProject/BioSample/SRA XML-based submission.
 */
public class AIRRTemplate2SRAConverter
{
  /**
   * The {@link org.metadatacenter.submission.biosample.AIRRTemplate} class is generated by jsonschema2pojo from the
   * AIRRTemplate.json JSON Schema file in the resources directory.
   *
   * @param airrInstance An AIRR template instance
   * @return A string containing a BioSample-conformant XML representation of the supplied AIRR instance
   * @throws DatatypeConfigurationException If a configuration error occurs during processing
   * @throws JAXBException                  If a JAXB error occurs during processing
   */
  public String generateSRASubmissionXMLFromAIRRTemplateInstance(AIRRTemplate airrInstance)
    throws DatatypeConfigurationException, JAXBException
  {
    final generated.ObjectFactory objectFactory = new generated.ObjectFactory();
    final biosample.ObjectFactory bioSampleObjectFactory = new biosample.ObjectFactory();
    final common.sp.ObjectFactory spCommonObjectFactory = new common.sp.ObjectFactory();

    TypeSubmission xmlSubmission = objectFactory.createTypeSubmission();

    // BioProject ID
    TypeBioProjectID bioProjectID = objectFactory.createTypeBioProjectID();
    bioProjectID.setBioProjectID(bioProjectID);
    bioProjectID.setBioProjectID(airrInstance.getProjectDescription().getBioProjectID().getValue()); //BioProject ID
    
    // Project Title
    TypeProjectTitle projectTitle = objectFactory.createProjectTitle();
    projectTitle.setProjectTitle(projectTitle);
    projectTitle.setProjectTitle(airrInstance.getProjectDescription().getProjectTitle().getValue()); // proejct title
    
    
    // Submission/Description/public description
    TypeSubmission.Description description = objectFactory.createTypeSubmissionDescription();
    xmlSubmission.setDescription(PublicDescription); // Public description fo the project
    description.setComment(airrInstance.getProjectDescription().getComment().getValue());  // public description
    
    // Submission/Description/project data type
    TypeProjectDataType projectDataType = objectFactory.createProjectDataType();
    projectDataType.setDataType(projectDataType);
    projectDataType.setDataType(airrInstance.getProjectDescription().getProjectDataType().getValue()); // proejct data type
    
    // Submission/Description/sample scope
    TypeSampelScope sampleScope = objectFactory.createTypeSampleScope();
    sampleScope.setSampleScope(sampleScope);
    sampleScope.setSampleScope(airrInstance.getProjectDescription().getSampleScope().getValue()); // proejct sample scope
       
    // Submission/Description/Organization/ContactInfo/Name
    TypeName name = objectFactory.createTypeName();
    contactInfo.setName(name);
    name.setFirst(airrInstance.getProjectDescription().getFirstName().getValue()); // first name
    name.setLast(airrInstance.getProjectDescription().getLastName().getValue()); // last name
    
    // Submission/Description/Organization/ContactInfo/email
    TypeContactInfo contactInfo = objectFactory.createTypeContactInfo();
    organization.getContact().add(contactInfo);
    contactInfo.setEmail(airrInstance.getProjectDescription().getEmail().getValue()); // e-mail
    
    // Submission/Description/Organization/Name
    // Submission/Description/Organization
    TypeOrganization organization = objectFactory.createTypeOrganization(); 
    description.getOrganization().add(organization);
    organization.setRole("master");
    organization.setType("institute");
    
    TypeOrganization.Name organizationName = objectFactory.createTypeOrganizationName();
    organization.setName(submittingOrganization);
    organizationName.setValue(airrInstance.getProjectDescription().getSubmittingOrganization().getValue()); // Submitting Organization

 // Submission/Description/Department/Name
    TypeDepartment department = objectFactory.createTypeDepartment();
    description.getDepartment().add(department);
    TypeDepartment.Name departmentName = objectFactory.createTypeDepartmentName();
    department.setName(department);
    departmentName.setValue(airrInstance.getProjectDescription().getDepartment().getValue()); // department
    
    // ADD OPTIONAL BIOPROJECT ATTRIBUTE LOOP
   
    // Submission/Action[1] - BioSample
    TypeSubmission.Action bioSampleAction = objectFactory.createTypeSubmissionAction();
    xmlSubmission.getAction().add(bioSampleAction);

    // Submission/Action[1]/AddData/target_db
    TypeSubmission.Action.AddData addData = objectFactory.createTypeSubmissionActionAddData();
    bioSampleAction.setAddData(addData);
    addData.setTargetDb("BioSample");

    // Submission/Action[1]/AddData/Data/content_type
    TypeSubmission.Action.AddData.Data data = objectFactory.createTypeSubmissionActionAddDataData();
    addData.getData().add(data);
    data.setContentType("XML");

    // Submission/Action[1]/AddData/Data/XMLContent
    TypeSubmission.Action.AddData.Data.XmlContent xmlContent = objectFactory.createTypeInlineDataXmlContent();
    data.setXmlContent(xmlContent);

    // Submission/Action[1]/AddData/Data/XMLContent/BioSample/schema_version
    TypeBioSample bioSample = bioSampleObjectFactory.createTypeBioSample();
    xmlContent.setBioSample(bioSample);
    bioSample.setSchemaVersion("2.0");

    // Submission/Action[1]/AddData/Data/XMLContent/BioSample/SampleID
    TypeBioSampleIdentifier sampleID = bioSampleObjectFactory.createTypeBioSampleIdentifier();
    bioSample.setSampleId(sampleID);

    // Submission/Action[1]/AddData/Data/XMLContent/BioSample/SampleID/SPUID
    TypeBioSampleIdentifier.SPUID spuid = bioSampleObjectFactory.createTypeBioSampleIdentifierSPUID();
    sampleID.getSPUID().add(spuid);
    spuid.setSpuidNamespace("CEDAR-NCBI"); 
    spuid.setValue(airrInstance.getProjectDescription().getBioProjectID().getValue()); 
    
    // Submission/Action[1]/AddData/Data/XMLContent/BioSample/Descriptor
    TypeDescriptor descriptor = spCommonObjectFactory.createTypeDescriptor();
    bioSample.setDescriptor(descriptor);
    descriptor.setTitle("CEDAR-NCBI Example instance of mythania gravis study");

    TypeRefId bioProject = spCommonObjectFactory.createTypeRefId();
    bioSample.getBioProject().add(bioProject);

    // Submission/Action[1]/AddData/Data/XMLContent/BioSample/BioProject/PrimaryID
    TypePrimaryId bioProjectPrimaryID = spCommonObjectFactory.createTypePrimaryId();
    bioProject.setPrimaryId(bioProjectPrimaryID);
    bioProjectPrimaryID.setDb("BioProject");
    bioProjectPrimaryID.setValue(airrInstance.getProjectDescription().getBioProjectID().getValue());

    // Submission/Action[1]/AddData/Data/XMLContent/BioSample/Package
    bioSample.setPackage("Human.1.0"); // TODO Is this hard coded for AIRR? //Could be get from datatype part of bioproject

    //RE-CHECK LOOPS VARIABLES
    
    for (aIRRBioSampleAttributes bioSampleAttributes : airrInstance.getAIRRBioSampleAttributes()
          .getaIRRBioSampleAttributes()){
    
    // Submission/Action[1]/AddData/Data/XMLContent/BioSample/Attributes
    TypeBioSample.Attributes bioSampleAttributes = bioSampleObjectFactory.createTypeBioSampleAttributes();
    bioSample.setAttributes(bioSampleAttributes);
    
    // Submission/Action[1]/AddData/Data/XMLContent/BioSample/Attributes/Attribute - AIRR BioSample attributes
    
    // New add
    TypeAttribute attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("projectedReleaseDate");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getProjectedReleaseDate().getValue());
    
    // New add
    TypeAttribute attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("sampleType");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getSampleType().getValue());
    
    // New add
    TypeAttribute attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("sampleName");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getSampleName().getValue()); 
    
    // new add
    attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("isolate");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getIsolate().getValue());
    
    // New add
    TypeAttribute attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("organism");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getOrganism().getValue());
    
    attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("age");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getAge().getValue());
    
    attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("biomaterialProvider");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getBiomaterialProvider().getValue());
    
    attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("sex");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getSex().getValue());

    attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("tissue");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getTissue().getValue());
    
    //Added new
    attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("phenotype");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getPhenotype().getValue());
        
    //Added new
     attribute = bioSampleObjectFactory.createTypeAttribute();
     bioSampleAttributes.getAttribute().add(attribute);
     attribute.setAttributeName("cellType");
     attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getCellType().getValue());
  
     
   //Added new
     attribute = bioSampleObjectFactory.createTypeAttribute();
     bioSampleAttributes.getAttribute().add(attribute);
     attribute.setAttributeName("cellSubType");
     attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getCellSubType().getValue());
  
   //Added new
     attribute = bioSampleObjectFactory.createTypeAttribute();
     bioSampleAttributes.getAttribute().add(attribute);
     attribute.setAttributeName("disease");
     attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getDisease().getValue());
          
    // new add
    attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("diseaseStage");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getDiseaseStage().getValue());
    
    // new add
    attribute = bioSampleObjectFactory.createTypeAttribute();
    bioSampleAttributes.getAttribute().add(attribute);
    attribute.setAttributeName("healthyState");
    attribute.setValue(airrInstance.getAIRRBioSampleAttributes().getHealthyState().getValue());
    
    //RE-CHECK LOOPS VARIABLES
    
    for (OptionalBioSampleAttribute optionalAttribute : airrInstance.getAIRRBioSampleAttributes()
      .getOptionalBioSampleAttribute()) {
      attribute = bioSampleObjectFactory.createTypeAttribute();
      bioSampleAttributes.getAttribute().add(attribute);
      attribute.setAttributeName(optionalAttribute.getName().getValue());
      attribute.setValue(optionalAttribute.getValue().getValue());
    }
  }
    
    //RE-CHECK LOOPS VARIABLES    
    for (aIRRSRAAttributes SRAAttributes : airrInstance.getSRAAttributes()
              .getaIRRSRAAttributes()){
      
    // Submission/Action[1]/AddData/Data/XMLContent/BioSample/Attributes
    TypeBioSample.Attributes sraAttributes = bioSampleObjectFactory.createTypeBioSampleAttributes();

    // Submission/Action[2] - SRA
    TypeSubmission.Action sraAction = objectFactory.createTypeSubmissionAction();
    xmlSubmission.getAction().add(sraAction);

    // Submission/Action[2]/AddFiles/target_db
    TypeSubmission.Action.AddFiles sraAddFiles = objectFactory.createTypeSubmissionActionAddFiles();
    sraAction.setAddFiles(sraAddFiles);
    sraAddFiles.setTargetDb("SRA");
    // TODO Set attribute CDE ID?

    // Submission/Action[2]/AddFiles/File
    TypeSubmission.Action.AddFiles.File sraFile = objectFactory.createTypeSubmissionActionAddFilesFile();
    sraAddFiles.getFile().add(sraFile);

    // Submission/Action[1]/AddFiles/Attributes/Attribute - AIRR SRA attributes
    
    TypeFileAttribute fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("sampleName");
    fileAttribute.setValue(airrInstance.getSRA().getSampleName().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);
    
    TypeFileAttribute fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("libraryID");
    fileAttribute.setValue(airrInstance.getSRA().getLibraryID().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);

    fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("libraryTitle");
    fileAttribute.setValue(airrInstance.getSRA().getLibraryTitle().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);
    
    fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("libraryStartegy");
    fileAttribute.setValue(airrInstance.getSRA().getLibraryStrategy().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);
    
    fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("librarySource");
    fileAttribute.setValue(airrInstance.getSRA().getLibrarySource().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);
    
    fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("librarySelection");
    fileAttribute.setValue(airrInstance.getSRA().getLibrarySelection().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);
    
    fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("libraryLayout");
    fileAttribute.setValue(airrInstance.getSRA().getLibraryLayout().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);
       
    fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("platform");
    fileAttribute.setValue(airrInstance.getSRA().getPlatform().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);

    
    fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("instrumentModel");
    fileAttribute.setValue(airrInstance.getSRA().getInstrumentModel().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);

    //new added
    fileAttribute = objectFactory.createTypeFileAttribute();
    fileAttribute.setName("designDescription");
    fileAttribute.setValue(airrInstance.getSRA().getDesignDescription().getValue());
    sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);  
     
    // File name and type (multiple) new added
    //RE-CHECK LOOPS VARIABLES
    for (rawSequenceFileInformation FileInformation : airrInstance.getSRA()
          .getRawSequenceFileInformation()) {

        fileAttribute = objectFactory.createTypeFileAttribute();
        fileAttribute.setName("FileType");
        fileAttribute.setValue(FileInformation.getSRA().getFileType().getValue());
        sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);
        
        fileAttribute = objectFactory.createTypeFileAttribute();
        fileAttribute.setName("FileName");
        fileAttribute.setValue(FileInformation.getSRA().getFileName().getValue());
        sraAddFiles.getAttributeOrMetaOrAttributeRefId().add(fileAttribute);
          }
    }  
       
      StringWriter writer = new StringWriter();

    JAXBElement<TypeSubmission> submissionRoot = objectFactory.createSubmission(xmlSubmission);
    JAXBContext ctx = JAXBContext.newInstance(TypeSubmission.class);
    Marshaller marshaller = ctx.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    //marshaller.marshal(submissionRoot, System.out);
    marshaller.marshal(submissionRoot, writer);

    return writer.toString();
  }

  private XMLGregorianCalendar createXMLGregorianCalendar(String date) throws DatatypeConfigurationException
  {
    DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
    GregorianCalendar gc = new GregorianCalendar();

    return datatypeFactory.newXMLGregorianCalendar(gc);
  }
}
