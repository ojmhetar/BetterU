/*
 * Created by Timothy Street on 2016.05.02  * 
 * Copyright © 2016 Timothy Street. All rights reserved. * 
 */
package com.betteru.managers;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import com.betteru.sessionbeanpackage.*;
import com.betteru.sourcepackage.DailyChallenges;
import com.betteru.sourcepackage.User;
import com.betteru.sourcepackage.UserIndex;
import com.betteru.sourcepackage.WeeklyChallenges;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

/**
 *
 * @author Tim
 */
@Named(value = "challengesManager")
@SessionScoped
public class ChallengesManager implements Serializable {

    private User selected;
    private List<DailyChallenges> currentUserDailyChallenges;
    private WeeklyChallenges currentUserWeeklyChallenge;

    /**
     * The instance variable 'UserIndexFacade' is annotated with the @EJB
     * annotation. This means that the GlassFish application server, at runtime,
     * will inject in this instance variable a reference to the @SessionScoped
     * session bean UserIndexFacade.
     */
    @EJB
    private UserIndexFacade userIndexFacade;

    /**
     * The instance variable 'DailyChallengesFacade' is annotated with the @EJB
     * annotation. This means that the GlassFish application server, at runtime,
     * will inject in this instance variable a reference to the @SessionScoped
     * session bean DailyChallengesFacade.
     */
    @EJB
    private DailyChallengesFacade dailyChallengesFacade;

    /**
     * The instance variable 'WeeklyChallengesFacade' is annotated with the @EJB
     * annotation. This means that the GlassFish application server, at runtime,
     * will inject in this instance variable a reference to the @SessionScoped
     * session bean WeeklyChallengesFacade.
     */
    @EJB
    private WeeklyChallengesFacade weeklyChallengesFacade;

    /**
     * The instance variable 'UserFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @SessionScoped session bean
     * UserFacade.
     */
    @EJB
    private UserFacade userFacade;

    public ChallengesManager() {
    }

    public List<DailyChallenges> getCurrentUserDailyChallenges() {
        return currentUserDailyChallenges;
    }

    public void setCurrentUserDailyChallenges(List<DailyChallenges> currentUserDailyChallenges) {
        this.currentUserDailyChallenges = currentUserDailyChallenges;
    }

    public WeeklyChallenges getCurrentUserWeeklyChallenge() {
        return currentUserWeeklyChallenge;
    }

    public void setCurrentUserWeeklyChallenge(WeeklyChallenges currentUserWeeklyChallenge) {
        this.currentUserWeeklyChallenge = currentUserWeeklyChallenge;
    }
    
    public void setChallengesDisplay() {
        User selectedUser = this.getSelected();
        if(selected != null) {
            List<UserIndex> userIndices = userIndexFacade.
                    retrieveEntriesForUserId(selectedUser.getId());
            currentUserDailyChallenges = new ArrayList<DailyChallenges>();

            for(UserIndex u: userIndices) {
                if(!u.getUserIndexPK().getChallengeType().equals("Weekly")) {
                    currentUserDailyChallenges.add(dailyChallengesFacade.getChallengeAtIndWithType(u.getInd(), u.getUserIndexPK().getChallengeType()));
                } else {
                    currentUserWeeklyChallenge = weeklyChallengesFacade.findWeeklyChallengeWithInd(u.getInd());
                }
            }
        }
    }

    public User getSelected() {
        selected = userFacade.find(FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user_id"));
        return selected;
    }
}