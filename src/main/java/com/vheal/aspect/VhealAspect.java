package com.vheal.aspect;

import com.vheal.entity.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class VhealAspect {

    private Logger myLogger = Logger.getLogger(getClass().getName());

    @Before("execution(public String processRegister(..))")
    public void userAndRole(JoinPoint theJoinPoint){

        MethodSignature methodSig = (MethodSignature) theJoinPoint.getSignature();

        myLogger.info("Method: " + methodSig);
        myLogger.info("\n=====>>> New User Registered");

        // display method arguments

        // get args
        Object[] args = theJoinPoint.getArgs();

        // loop thru args
        for (Object tempArg : args) {
//            myLogger.info(tempArg.toString());

            if (tempArg instanceof User) {

                // downcast and print User specific stuff
                User theUser = (User) tempArg;

                myLogger.info("\n=====>>> User Email= " + theUser.getEmail());
                myLogger.info("\n=====>>> User Password= " + theUser.getPassword());
                myLogger.info("\n=====>>> User " + args[0]);

            }
        }

    }

    // Detect Login
    @After("execution(* com.vheal.controller.LoginController.processLogin(..))")
    public void loginDetector(JoinPoint theJoinPoint) {

        myLogger.info("\n=====>>> New Login Detected");

    }

    // Log after Doctor details are saved
    @AfterReturning(
            pointcut="execution(* com.vheal.controller.DoctorController.saveDoctor(..))",
            returning="result")
    public void savedDoctorDetails(
            JoinPoint theJoinPoint, String result) {
        // get args
        Object[] args = theJoinPoint.getArgs();

        myLogger.info("\n=====>>>" + args[0]);
        myLogger.info("\n=====>>> Saved Doctor Details ");

    }

    // Log after Patient details are saved
    @AfterReturning(
            pointcut="execution(* com.vheal.controller.PatientController.savePatient(..))",
            returning="result")
    public void savedPatientDetails(
            JoinPoint theJoinPoint, String result) {
        // get args
        Object[] args = theJoinPoint.getArgs();

        myLogger.info("\n=====>>>" + args[0]);
        myLogger.info("\n=====>>> Saved Patient Details ");

    }

}
