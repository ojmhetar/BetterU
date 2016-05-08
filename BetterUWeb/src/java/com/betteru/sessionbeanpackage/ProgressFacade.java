/*
 * Created by Jared Schwalbe on 2016.04.08  * 
 * Copyright © 2016 Osman Balci. All rights reserved. * 
 */
package com.betteru.sessionbeanpackage;

import com.betteru.sourcepackage.Progress;
import com.betteru.sourcepackage.ProgressPK;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Jared
 */
@Stateless
public class ProgressFacade extends AbstractFacade<Progress> {

    @PersistenceContext(unitName = "BetterU-WebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgressFacade() {
        super(Progress.class);
    }
    
    /**
     * finds all progress entries for a user
     * @param userId - the user we want
     * @return a list of progress objects
     */
    public List<Progress> findAllProgressEntriesByUid(int userId) {
        String query = "SELECT p FROM Progress p WHERE p.progressPK.userId = :userId";
        
        if (em.createQuery(query)
                .setParameter("userId", userId)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (List<Progress>) (em.createQuery(query)
                .setParameter("userId", userId)
                .getResultList());        
        }
    }
    
    /**
     * Gets the progress data for a particular user for a particular week
     * @param userId - user we want
     * @param logDate - the last date of the week we want in epoch
     * @return list of progress objects
     */
    public List<Progress> findWeekByUid(int userId, long logDate) {
        long aWeekAgo = logDate - 604800 + 1;
        TypedQuery<Progress> query = em.createNamedQuery("Progress.findWeek", Progress.class)
                                        .setParameter("logDate", logDate).setParameter("userId", userId).setParameter("aWeekAgo", aWeekAgo);               
        return query.getResultList().isEmpty() ? null : query.getResultList();        
    }
    
    /**
     * Gets the progress data for a particular user for a particular month
     * @param userId - user we want
     * @param logDate - the last date of the month we want in epoch
     * @param numDaysInMonth - number of days we want in the month of data
     * @return list of progress objects
     */
    public List<Progress> findMonthByUid(int userId, long logDate, int numDaysInMonth) {
        long aMonthAgo = logDate - (24*60*60*numDaysInMonth) + 1;
        TypedQuery<Progress> query = em.createNamedQuery("Progress.findMonth", Progress.class)
                                        .setParameter("logDate", logDate).setParameter("userId", userId).setParameter("aMonthAgo", aMonthAgo);               
        return query.getResultList().isEmpty() ? null : query.getResultList();        
    }
       
    /**
     * Get specific progress object
     * @param user_id - the user we want
     * @param epochTime - the date we want in epoch time
     * @return the selected progress object
     */
    public Progress getProgressEntry(Integer user_id, Integer epochTime) {
        if (em.createQuery("SELECT p FROM Progress p WHERE p.progressPK.userId = :userId AND p.progressPK.logDate = :time")
                .setParameter("userId", user_id)
                .setParameter("time", epochTime)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (Progress) (em.createQuery("SELECT p FROM Progress p WHERE p.progressPK.userId = :userId AND p.progressPK.logDate = :time")
                .setParameter("userId", user_id)
                .setParameter("time", epochTime)
                .getSingleResult());       
        }
    }
}
