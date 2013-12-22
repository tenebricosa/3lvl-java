package DAO;

import DAO.Impl.BioDAOImpl;

public class Factory {

    private static BioDAO BioDAO = null;
    private static Factory instance = null;

    public static synchronized Factory getInstance(){
        if (instance == null){
            instance = new Factory();
        }
        return instance;
    }

    public BioDAO getBioDAO(){
        if (BioDAO == null){
            BioDAO = new BioDAOImpl();
        }
        return BioDAO;
    }
}