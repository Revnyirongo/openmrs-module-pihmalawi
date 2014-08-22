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
package org.openmrs.module.pihmalawi.reporting.library;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.module.pihmalawi.metadata.HivMetadata;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.Age;
import org.openmrs.module.reporting.definition.library.BaseDefinitionLibrary;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Defines all of the Cohort Definition instances we want to expose for Pih Malawi
 */
@Component
public class HivCohortDefinitionLibrary extends BaseDefinitionLibrary<CohortDefinition> {

    public static final String PREFIX = "pihmalawi.cohortDefinition.hiv.";

	@Autowired
	private DataFactory df;

    @Autowired
    private HivMetadata hivMetadata;

    @Override
    public Class<? super CohortDefinition> getDefinitionType() {
        return CohortDefinition.class;
    }

    @Override
    public String getKeyPrefix() {
        return PREFIX;
    }

	@DocumentedDefinition(value = "hasAnHccNumber")
	public CohortDefinition getPatientsWithAnHccNumber() {
		return df.getPatientsWithIdentifierOfType(hivMetadata.getHccNumberIdentifierType());
	}

	@DocumentedDefinition(value = "hasAnHccNumberAtLocation")
	public CohortDefinition getPatientsWithAnHccNumberAtLocation() {
		return df.getPatientsWithIdentifierOfTypeAtLocation(hivMetadata.getHccNumberIdentifierType());
	}

	@DocumentedDefinition(value = "hasAnArvNumber")
	public CohortDefinition getPatientsWithAnArvNumber() {
		return df.getPatientsWithIdentifierOfType(hivMetadata.getArvNumberIdentifierType());
	}

	@DocumentedDefinition(value = "hasAnyPreArvNumber")
	public CohortDefinition getPatientsWithAnyPreArvNumber() {
		return df.getPatientsWithIdentifierOfType(hivMetadata.getHccNumberIdentifierType(), hivMetadata.getOldPartNumberIdentifierType(), hivMetadata.getOldPreArtNumberOldFormatIdentifierType());
	}

	@DocumentedDefinition(value = "enrolledInHivProgramByEndDate")
	public CohortDefinition getEverEnrolledInHivProgramByEndDate() {
		return df.getEverEnrolledInProgramByEndDate(hivMetadata.getHivProgram());
	}

	@DocumentedDefinition(value = "activelyEnrolledInHivProgramOnEndDate")
	public CohortDefinition getActivelyEnrolledInHivProgramAtLocationOnEndDate() {
		return df.getActivelyEnrolledInProgramAtLocationOnEndDate(hivMetadata.getHivProgram());
	}

	@DocumentedDefinition(value = "enrolledInHivProgramDuringPeriod")
	public CohortDefinition getEnrolledInHivProgramDuringPeriod() {
		return df.getEnrolledInProgramDuringPeriod(hivMetadata.getHivProgram());
	}

	@DocumentedDefinition(value = "startedPreArtWithHccNumberAtLocationInPeriod")
	public CohortDefinition getStartedPreArtWithHccNumberAtLocationInPeriod() {
		CohortDefinition startedPreArtState = df.getStartedInStateAtLocationDuringPeriod(hivMetadata.getPreArtState());
		CohortDefinition hasHccNumber = getPatientsWithAnHccNumberAtLocation();
		return df.getPatientsInAll(startedPreArtState, hasHccNumber);
	}

	@DocumentedDefinition(value = "startedPreArtWithHccNumberAtLocationByEndDate")
	public CohortDefinition getStartedPreArtWithHccNumberAtLocationByEndDate() {
		CohortDefinition everPreArtState = df.getEverEnrolledInStateAtLocationByEndDate(hivMetadata.getPreArtState());
		CohortDefinition hasHccNumber = getPatientsWithAnHccNumberAtLocation();
		return df.getPatientsInAll(everPreArtState, hasHccNumber);
	}

	@DocumentedDefinition(value = "inPreArtOrExposedChildStateWithHccNumberAtLocationOnEndDate")
	public CohortDefinition getInPreArtOrExposedChildStateWithHccNumberAtLocationOnEndDate() {
		List<ProgramWorkflowState> l = Arrays.asList(hivMetadata.getPreArtState(), hivMetadata.getExposedChildState());
		CohortDefinition inPreArtOrExposedChild = df.getActiveInStatesAtLocationOnEndDate(l);
		CohortDefinition hasHccNumber = getPatientsWithAnHccNumberAtLocation();
		return df.getPatientsInAll(inPreArtOrExposedChild, hasHccNumber);
	}

	@DocumentedDefinition(value = "inPreArtStateWithHccNumberAtLocationOnEndDate")
	public CohortDefinition getInPreArtStateWithHccNumberAtLocationOnEndDate() {
		CohortDefinition inPreArt = df.getActiveInStateAtLocationOnEndDate(hivMetadata.getPreArtState());
		CohortDefinition hasHccNumber = getPatientsWithAnHccNumberAtLocation();
		return df.getPatientsInAll(inPreArt, hasHccNumber);
	}

	@DocumentedDefinition(value = "startedExposedChildWithHccNumberAtLocationByEndDate")
	public CohortDefinition getStartedExposedChildWithHccNumberAtLocationByEndDate() {
		CohortDefinition everExposedChildState = df.getEverEnrolledInStateAtLocationByEndDate(hivMetadata.getExposedChildState());
		CohortDefinition hasHccNumber = getPatientsWithAnHccNumberAtLocation();
		return df.getPatientsInAll(everExposedChildState, hasHccNumber);
	}

	@DocumentedDefinition(value = "startedExposedChildWithHccNumberAtLocationInPeriod")
	public CohortDefinition getStartedExposedChildWithHccNumberAtLocationInPeriod() {
		CohortDefinition exposedInPeriod = df.getStartedInStateAtLocationDuringPeriod(hivMetadata.getExposedChildState());
		CohortDefinition hasHccNumber = getPatientsWithAnHccNumberAtLocation();
		return df.getPatientsInAll(exposedInPeriod, hasHccNumber);
	}

	@DocumentedDefinition(value = "everEnrolledInArtAtLocationByEndDate")
	public CohortDefinition getEverEnrolledInArtAtLocationByEndDate() {
		return df.getEverEnrolledInStateAtLocationByEndDate(hivMetadata.getOnArvsState());
	}

	@DocumentedDefinition(value = "startedOnArtStateAtLocationDuringPeriod")
	public CohortDefinition getStartedOnArtStateAtLocationDuringPeriod() {
		return df.getStartedInStateAtLocationDuringPeriod(hivMetadata.getOnArvsState());
	}

	@DocumentedDefinition(value = "inOnArtStateAtLocationOnEndDate")
	public CohortDefinition getInOnArtStateAtLocationOnEndDate() {
		return df.getActiveInStateAtLocationOnEndDate(hivMetadata.getOnArvsState());
	}

	@DocumentedDefinition(value = "inTransferredOutAtLocationOnEndDate")
	public CohortDefinition getInTransferredOutAtLocationOnEndDate() {
		return df.getActiveInStateAtLocationOnEndDate(hivMetadata.getTransferredOutState());
	}

	@DocumentedDefinition("inTransferredInternallyAtLocationOnEndDate")
	public CohortDefinition getInTransferredInternallyAtLocationOnEndDate() {
		return df.getActiveInStateAtLocationOnEndDate(hivMetadata.getTransferredInternallyState());
	}

	@DocumentedDefinition(value = "transitionedFromPreArtToArtAtLocationDuringPeriod")
	public CohortDefinition getTransitionedFromPreArtToArtAtLocationDuringPeriod() {
		CohortDefinition preArt = getStartedPreArtWithHccNumberAtLocationByEndDate();
		CohortDefinition art = getStartedOnArtStateAtLocationDuringPeriod();
		return df.getPatientsInAll(preArt, art);
	}

	@DocumentedDefinition(value = "transitionedFromPreArtToArtAtLocationByEnd")
	public CohortDefinition getTransitionedFromPreArtToArtAtLocationByEnd() {
		CohortDefinition preArt = getStartedPreArtWithHccNumberAtLocationByEndDate();
		CohortDefinition art = getEverEnrolledInArtAtLocationByEndDate();
		return df.getPatientsInAll(preArt, art);
	}

	@DocumentedDefinition(value = "transferredOutOfPreArtAtLocationByEnd")
	public CohortDefinition getTransferredOutOfPreArtAtLocationByEnd() {
		CohortDefinition preArt = getStartedPreArtWithHccNumberAtLocationByEndDate();
		CohortDefinition art = getEverEnrolledInArtAtLocationByEndDate();
		CohortDefinition txOut = getInTransferredOutAtLocationOnEndDate();
		return df.createPatientComposition(preArt, "AND", txOut, "AND NOT", art);
	}

	@DocumentedDefinition(value = "diedWhilePreArtAtLocationByEnd")
	public CohortDefinition getDiedWhilePreArtAtLocationByEnd() {
		CohortDefinition preArt = getStartedPreArtWithHccNumberAtLocationByEndDate();
		CohortDefinition art = getEverEnrolledInArtAtLocationByEndDate();
		CohortDefinition died = getInDiedStateAtLocationOnEndDate();
		return df.createPatientComposition(preArt, "AND", died, "AND NOT", art);
	}

	@DocumentedDefinition(value = "inArtAndLastAppointmentDate3WeeksOrMoreByEndDate")
	public CohortDefinition getInArtAndLastAppointmentDate3WeeksOrMoreByEndDate() {
		CohortDefinition onArt = getInOnArtStateAtLocationOnEndDate();
		CohortDefinition missing = df.getPatientsWhoseMostRecentObsDateIsOlderThanValueAtLocationByEndDate(hivMetadata.getAppointmentDateConcept(), Arrays.asList(hivMetadata.getArtFollowupEncounterType()), "3w");
		return df.getPatientsInAll(onArt, missing);
	}

	@DocumentedDefinition(value = "inArtAndLastAppointmentDate2MonthsOrMoreByEndDate")
	public CohortDefinition getInArtAndLastAppointmentDate2MonthsOrMoreByEndDate() {
		CohortDefinition onArt = getInOnArtStateAtLocationOnEndDate();
		CohortDefinition missing = df.getPatientsWhoseMostRecentObsDateIsOlderThanValueAtLocationByEndDate(hivMetadata.getAppointmentDateConcept(), Arrays.asList(hivMetadata.getArtFollowupEncounterType()), "2m");
		return df.getPatientsInAll(onArt, missing);
	}

	@DocumentedDefinition(value = "lastPreArtOrExposedAppointmentDate8weeksOrMoreByEndDate")
	public CohortDefinition getLastPreArtOrExposedAppointmentDate8weeksOrMoreByEndDate() {
		List<EncounterType> types = Arrays.asList(hivMetadata.getPreArtFollowupEncounterType(), hivMetadata.getExposedChildFollowupEncounterType());
		return df.getPatientsWhoseMostRecentObsDateIsOlderThanValueAtLocationByEndDate(hivMetadata.getAppointmentDateConcept(), types, "8w");
	}

	@DocumentedDefinition(value = "startedDefaultedStateAtLocationDuringPeriod")
	public CohortDefinition getStartedDefaultedStateAtLocationDuringPeriod() {
		return df.getStartedInStateAtLocationDuringPeriod(hivMetadata.getDefaultedState());
	}

	@DocumentedDefinition(value = "inDefaultedStateAtLocationOnEndDate")
	public CohortDefinition getInDefaultedStateAtLocationOnEndDate() {
		return df.getActiveInStateAtLocationOnEndDate(hivMetadata.getDefaultedState());
	}

	@DocumentedDefinition(value = "everArtDefaultedDuringPeriodAndStillDefaultedOnEndDate")
	public CohortDefinition getEverArtDefaultedDuringPeriodAndStillDefaultedOnEndDate() {
		CohortDefinition artEver = getEverEnrolledInArtAtLocationByEndDate();
		CohortDefinition defaultedDuring = getStartedDefaultedStateAtLocationDuringPeriod();
		CohortDefinition defaultedAtEnd = getInDefaultedStateAtLocationOnEndDate();
		return df.getPatientsInAll(artEver, defaultedDuring, defaultedAtEnd);
	}

	@DocumentedDefinition(value = "startedDiedStateAtLocationDuringPeriod")
	public CohortDefinition getStartedDiedStateAtLocationDuringPeriod() {
		return df.getStartedInStateAtLocationDuringPeriod(hivMetadata.getDiedState());
	}

	@DocumentedDefinition(value = "inDiedStateAtLocationOnEndDate")
	public CohortDefinition getInDiedStateAtLocationOnEndDate() {
		return df.getActiveInStateAtLocationOnEndDate(hivMetadata.getDiedState());
	}

	@DocumentedDefinition(value = "hadPreArtEncounterBeforeStartDate")
	public CohortDefinition getPatientsWithAPreArtEncounterBeforeStartDate() {
		return df.getAnyEncounterOfTypesBeforeStartDate(hivMetadata.getPreArtEncounterTypes());
	}

	@DocumentedDefinition(value = "hadPreArtEncounterDuringPeriod")
	public CohortDefinition getPatientsWithAPreArtEncounterDuringPeriod() {
		return df.getAnyEncounterOfTypesDuringPeriod(hivMetadata.getPreArtEncounterTypes());
	}

	@DocumentedDefinition(value = "hadPreArtEncounterByEndDate")
	public CohortDefinition getPatientsWithAPreArtEncounterByEndDate() {
		return df.getAnyEncounterOfTypesByEndDate(hivMetadata.getPreArtEncounterTypes());
	}

	@DocumentedDefinition(value = "hadPreArtInitialEncounterByEndDate")
	public CohortDefinition getPatientsWithAPreArtInitialEncounterByEndDate() {
		return df.getAnyEncounterOfTypesByEndDate(Arrays.asList(hivMetadata.getPreArtInitialEncounterType()));
	}

	@DocumentedDefinition(value = "hadPreArtFollowupEncounterByEndDate")
	public CohortDefinition getPatientsWithAPreArtFollowupEncounterByEndDate() {
		return df.getAnyEncounterOfTypesByEndDate(Arrays.asList(hivMetadata.getPreArtFollowupEncounterType()));
	}

	@DocumentedDefinition(value = "hadArtEncounterBeforeStartDate")
	public CohortDefinition getPatientsWithAnArtEncounterBeforeStartDate() {
		return df.getAnyEncounterOfTypesBeforeStartDate(hivMetadata.getArtEncounterTypes());
	}

	@DocumentedDefinition(value = "hadArtEncounterDuringPeriod")
	public CohortDefinition getPatientsWithAnArtEncounterDuringPeriod() {
		return df.getAnyEncounterOfTypesDuringPeriod(hivMetadata.getArtEncounterTypes());
	}

	@DocumentedDefinition(value = "hadArtEncounterByEndDate")
	public CohortDefinition getPatientsWithAnArtEncounterByEndDate() {
		return df.getAnyEncounterOfTypesByEndDate(hivMetadata.getArtEncounterTypes());
	}

	@DocumentedDefinition(value = "hadArtInitialEncounterByEndDate")
	public CohortDefinition getPatientsWithAnArtInitialEncounterByEndDate() {
		return df.getAnyEncounterOfTypesByEndDate(Arrays.asList(hivMetadata.getArtInitialEncounterType()));
	}

	@DocumentedDefinition(value = "hadArtFollowupEncounterByEndDate")
	public CohortDefinition getPatientsWithAnArtFollowupEncounterByEndDate() {
		return df.getAnyEncounterOfTypesByEndDate(Arrays.asList(hivMetadata.getArtFollowupEncounterType()));
	}

	@DocumentedDefinition(value = "hadHivEncounterBeforeStartDate")
	public CohortDefinition getPatientsWithAnHivEncounterBeforeStartDate() {
		return df.getAnyEncounterOfTypesBeforeStartDate(hivMetadata.getHivEncounterTypes());
	}

	@DocumentedDefinition(value = "hadHivEncounterDuringPeriod")
	public CohortDefinition getPatientsWithAnHivEncounterDuringPeriod() {
		return df.getAnyEncounterOfTypesDuringPeriod(hivMetadata.getHivEncounterTypes());
	}

	@DocumentedDefinition(value = "hadHivEncounterByEndDate")
	public CohortDefinition getPatientsWithAnHivEncounterByEndDate() {
		return df.getAnyEncounterOfTypesByEndDate(hivMetadata.getHivEncounterTypes());
	}

	@DocumentedDefinition(value = "inExposedChildStateOnEndDate")
	public CohortDefinition getPatientsInExposedChildStateOnEndDate() {
		return df.getCurrentlyInStateOnEndDate(hivMetadata.getExposedChildState());
	}

	@DocumentedDefinition(value = "inPreArtStateOnEndDate")
	public CohortDefinition getPatientsInPreArtStateOnEndDate() {
		return df.getCurrentlyInStateOnEndDate(hivMetadata.getPreArtState());
	}

	@DocumentedDefinition(value = "inOnArvsStateOnEndDate")
	public CohortDefinition getPatientsInOnArvsStateOnEndDate() {
		return df.getCurrentlyInStateOnEndDate(hivMetadata.getOnArvsState());
	}

	@DocumentedDefinition(value = "everInExposedChildStateByEndDate")
	public CohortDefinition getPatientsEverInExposedChildStateByEndDate() {
		return df.getEverInStateByEndDate(hivMetadata.getExposedChildState());
	}

	@DocumentedDefinition(value = "everInPreArtStateByEndDate")
	public CohortDefinition getPatientsEverInPreArtStateByEndDate() {
		return df.getEverInStateByEndDate(hivMetadata.getPreArtState());
	}

	@DocumentedDefinition(value = "everInOnArvsStateByEndDate")
	public CohortDefinition getPatientsEverInOnArvsStateByEndDate() {
		return df.getEverInStateByEndDate(hivMetadata.getOnArvsState());
	}

	@DocumentedDefinition(value = "0to1MonthsOldAtPreArtStateStartAtLocationByEndDate")
	public CohortDefinition getPatients0to1MonthsOldAtPreArtStateStartAtLocationByEndDate() {
		return df.getPatientsWhoStartedStateWhenInAgeRangeAtLocationByEndDate(hivMetadata.getPreArtState(), null, null, 1, Age.Unit.MONTHS);
	}

	@DocumentedDefinition(value = "2to23MonthsOldAtPreArtStateStartAtLocationByEndDate")
	public CohortDefinition getPatients2to23MonthsOldAtPreArtStateStartAtLocationByEndDate() {
		return df.getPatientsWhoStartedStateWhenInAgeRangeAtLocationByEndDate(hivMetadata.getPreArtState(), 2, Age.Unit.MONTHS, 23, Age.Unit.MONTHS);
	}

	@DocumentedDefinition(value = "2to14YearsOldAtPreArtStateStartAtLocationByEndDate")
	public CohortDefinition getPatients2to14YearsOldAtPreArtStateStartAtLocationByEndDate() {
		return df.getPatientsWhoStartedStateWhenInAgeRangeAtLocationByEndDate(hivMetadata.getPreArtState(), 2, Age.Unit.YEARS, 14, Age.Unit.YEARS);
	}

	@DocumentedDefinition(value = "15YearsUpAtPreArtStateStartAtLocationByEndDate")
	public CohortDefinition getPatients15YearsUpAtPreArtStateStartAtLocationByEndDate() {
		return df.getPatientsWhoStartedStateWhenInAgeRangeAtLocationByEndDate(hivMetadata.getPreArtState(), 15, Age.Unit.YEARS, null, null);
	}

	@DocumentedDefinition(value = "0to1MonthsOldAtExposedChildStateStartAtLocationByEndDate")
	public CohortDefinition getPatients0to1MonthsOldAtExposedChildStateStartAtLocationByEndDate() {
		return df.getPatientsWhoStartedStateWhenInAgeRangeAtLocationByEndDate(hivMetadata.getExposedChildState(), null, null, 1, Age.Unit.MONTHS);
	}

	@DocumentedDefinition(value = "2to23MonthsOldAtExposedChildStateStartAtLocationByEndDate")
	public CohortDefinition getPatients2to23MonthsOldAtExposedChildStateStartAtLocationByEndDate() {
		return df.getPatientsWhoStartedStateWhenInAgeRangeAtLocationByEndDate(hivMetadata.getExposedChildState(), 2, Age.Unit.MONTHS, 23, Age.Unit.MONTHS);
	}

	@DocumentedDefinition(value = "2to14YearsOldAtExposedChildStateStartAtLocationByEndDate")
	public CohortDefinition getPatients2to14YearsOldAtExposedChildStateStartAtLocationByEndDate() {
		return df.getPatientsWhoStartedStateWhenInAgeRangeAtLocationByEndDate(hivMetadata.getExposedChildState(), 2, Age.Unit.YEARS, 14, Age.Unit.YEARS);
	}

	@DocumentedDefinition(value = "15YearsUpAtExposedChildStateStartAtLocationByEndDate")
	public CohortDefinition getPatients15YearsUpAtExposedChildStateStartAtLocationByEndDate() {
		return df.getPatientsWhoStartedStateWhenInAgeRangeAtLocationByEndDate(hivMetadata.getExposedChildState(), 15, Age.Unit.YEARS, null, null);
	}

	@DocumentedDefinition(value = "hadWeightAtHivEncounterDuringPeriod")
	public CohortDefinition getPatientsWithWeightAtHivEncounterDuringPeriod() {
		return df.getPatientsWithAnyObsDuringPeriod(hivMetadata.getWeightConcept(), hivMetadata.getHivEncounterTypes());
	}

	@DocumentedDefinition(value = "hasPcrTestResultByEndDate")
	public CohortDefinition getPatientsWithPcrTestResultByEndDate() {
		List<Concept> validValues = Arrays.asList(hivMetadata.getPositiveConcept(), hivMetadata.getNegativeConcept(), hivMetadata.getIndeterminateConcept());
		CohortDefinition pcrTest = df.getPatientsWithCodedObsByEndDate(hivMetadata.getHivDnaPcrTestConcept(), validValues);
		CohortDefinition pcrResult = df.getPatientsWithCodedObsByEndDate(hivMetadata.getDnaPcrResultConcept(), validValues);
		CohortDefinition pcrResult2 = df.getPatientsWithCodedObsByEndDate(hivMetadata.getDnaPcrResult2Concept(), validValues);
		CohortDefinition pcrResult3 = df.getPatientsWithCodedObsByEndDate(hivMetadata.getDnaPcrResult3Concept(), validValues);
		return df.getPatientsInAny(pcrTest, pcrResult, pcrResult2, pcrResult3);
	}

	// Convenience methods

	public CohortDefinition getDiedAtLocationWithinMonthsOfEndDate(int numMonths) {
		CohortDefinition trueAtEnd = getInDiedStateAtLocationOnEndDate();
		CohortDefinition trueMonthsBefore = df.getActiveInStateAtLocationNumMonthsBeforeEndDate(hivMetadata.getDiedState(), numMonths);
		return df.createPatientComposition(trueAtEnd, "AND NOT", trueMonthsBefore);
	}

	public CohortDefinition getPatientsWithAPreArtEncounterWithinMonthsOfEndDate(int numMonths) {
		return df.getAnyEncounterOfTypesWithinMonthsByEndDate(hivMetadata.getPreArtEncounterTypes(), numMonths);
	}

	public CohortDefinition getPatientsWithAnArtEncounterWithinMonthsOfEndDate(int numMonths) {
		return df.getAnyEncounterOfTypesWithinMonthsByEndDate(hivMetadata.getArtEncounterTypes(), numMonths);
	}

    public CohortDefinition getPatientsWithCd4RecordedWithinMonthsOfEndDate(int numMonths) {
        CohortDefinition cd4 = df.getPatientsWithAnyObsWithinMonthsByEndDate(hivMetadata.getCd4CountConcept(), numMonths);
        CohortDefinition clinicianReported = df.getPatientsWithAnyObsWithinMonthsByEndDate(hivMetadata.getClinicianReportedCd4Concept(), numMonths);
        return df.getPatientsInAny(cd4, clinicianReported);
    }

	public CohortDefinition getPatientsWithCd4MeasuredInLabWithinMonthsOfEndDate(int numMonths) {
		return df.getPatientsWithAnyObsWithinMonthsByEndDate(hivMetadata.getCd4CountConcept(), numMonths);
	}


}
