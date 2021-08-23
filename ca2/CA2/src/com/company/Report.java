package com.company;
// An interface is an abstract class which we use to group empty, abstract methods
// Our superclass implements this interface, our children can then create their own version of this interface to suit their needs
// So in this way we force similar classes to have these same methods, just implemented in their own ways
public interface Report {
    // This will be used in the child classes to print ALL details of an object
    public abstract void printDetailedReport();
    // This will be used to only print a small number of an object's details
    public abstract void printSummary();
}
