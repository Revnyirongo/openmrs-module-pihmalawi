/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.pihmalawi.reporting.reports;

import org.openmrs.EncounterType;
import org.openmrs.PatientIdentifierType;
import org.openmrs.module.pihmalawi.metadata.HivMetadata;
import org.openmrs.module.pihmalawi.reporting.library.BaseEncounterDataLibrary;
import org.openmrs.module.pihmalawi.reporting.library.BasePatientDataLibrary;
import org.openmrs.module.pihmalawi.reporting.library.DataFactory;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.data.encounter.library.BuiltInEncounterDataLibrary;
import org.openmrs.module.reporting.data.patient.library.BuiltInPatientDataLibrary;
import org.openmrs.module.reporting.dataset.definition.EncounterAndObsDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.query.encounter.definition.BasicEncounterQuery;
import org.openmrs.module.reporting.query.encounter.definition.MappedParametersEncounterQuery;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.renderer.XlsReportRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class HivVisitsReport extends ApzuReportManager {

	public static final String EXCEL_REPORT_DESIGN_UUID = "6c5fcaf3-02d1-11e4-a73c-54ee7513a7ff";

	@Autowired
	private DataFactory df;

	@Autowired
	private HivMetadata metadata;

	@Autowired
	private BuiltInEncounterDataLibrary builtInEncounterData;

	@Autowired
	private BaseEncounterDataLibrary baseEncounterData;

	@Autowired
	private BuiltInPatientDataLibrary builtInPatientData;

	@Autowired
	private BasePatientDataLibrary basePatientData;

	public HivVisitsReport() {}

	@Override
	public String getUuid() {
		return "6d97d62c-02d1-11e4-a73c-54ee7513a7ff";
	}

	@Override
	public String getName() {
		return "HIV Visits";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public List<Parameter> getParameters() {
		List<Parameter> l = new ArrayList<Parameter>();
		l.add(new Parameter("startDate", "From Date", Date.class));
		l.add(new Parameter("endDate", "To Date", Date.class));
		return l;
	}

	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition rd = new ReportDefinition();
		rd.setUuid(getUuid());
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.setParameters(getParameters());
		addDataSet(rd, "art_initial", metadata.getArtInitialEncounterType(), metadata.getArvNumberIdentifierType());
		addDataSet(rd, "art_followup", metadata.getArtFollowupEncounterType(), metadata.getArvNumberIdentifierType());
		addDataSet(rd, "part_initial", metadata.getPreArtInitialEncounterType(), metadata.getHccNumberIdentifierType());
		addDataSet(rd, "part_followup", metadata.getPreArtFollowupEncounterType(), metadata.getHccNumberIdentifierType());
		addDataSet(rd, "exposed_child_initial", metadata.getExposedChildInitialEncounterType(), metadata.getHccNumberIdentifierType());
		addDataSet(rd, "exposed_child_followup", metadata.getExposedChildFollowupEncounterType(), metadata.getHccNumberIdentifierType());
		return rd;
	}

	protected void addDataSet(ReportDefinition rd, String key, EncounterType encounterType, PatientIdentifierType identifierType) {
		EncounterAndObsDataSetDefinition dsd = new EncounterAndObsDataSetDefinition();
		dsd.setParameters(getParameters());

		BasicEncounterQuery rowFilter = new BasicEncounterQuery();
		rowFilter.addEncounterType(encounterType);
		rowFilter.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		rowFilter.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		MappedParametersEncounterQuery q = new MappedParametersEncounterQuery(rowFilter, ObjectUtil.toMap("onOrAfter=startDate,onOrBefore=endDate"));
		dsd.addRowFilter(Mapped.mapStraightThrough(q));

		dsd.addColumn("ENCOUNTER_ID", builtInEncounterData.getEncounterId(), "");
		dsd.addColumn("ENCOUNTER_DATETIME", builtInEncounterData.getEncounterDatetime(), "");
		dsd.addColumn("LOCATION", builtInEncounterData.getLocationName(), "");
		dsd.addColumn("INTERNAL_PATIENT_ID", builtInEncounterData.getPatientId(), "");
		dsd.addColumn(identifierType.getName(), df.getAllIdentifiersOfType(identifierType, df.getIdentifierCollectionConverter()), "");
		dsd.addColumn("Birthdate", basePatientData.getBirthdate(), "");
		dsd.addColumn("Age at encounter (yr)", baseEncounterData.getAgeAtEncounterDateInYears(), "");
		dsd.addColumn("Age at encounter (mth)", baseEncounterData.getAgeAtEncounterDateInMonths(), "");
		dsd.addColumn("M/F", builtInPatientData.getGender(), "");
		dsd.addColumn("District", basePatientData.getDistrict(), "");
		dsd.addColumn("T/A", basePatientData.getTraditionalAuthority(), "");
		dsd.addColumn("Village", basePatientData.getVillage(), "");

		rd.addDataSetDefinition(key, Mapped.mapStraightThrough(dsd));
	}

	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		List<ReportDesign> l = new ArrayList<ReportDesign>();
		ReportDesign reportDesign = createExcelDesign(EXCEL_REPORT_DESIGN_UUID, reportDefinition);
		reportDesign.getProperties().setProperty(XlsReportRenderer.INCLUDE_DATASET_NAME_AND_PARAMETERS_PROPERTY, "false");
		l.add(reportDesign);
		return l;
	}

	@Override
	public String getVersion() {
		return "1.0-SNAPSHOT";
	}
}