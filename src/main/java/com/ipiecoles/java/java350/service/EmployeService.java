public void calculPerformanceCommercial(String matricule, Long caTraite, Long objectifCa) throws EmployeException {
    // Vérification des paramètres
    validateParamsPerformanceCommercial(matricule, caTraite, objectifCa);

    // Récupération de l'employé
    Employe employe = employeRepository.findByMatricule(matricule);
    if(employe == null){
        throw new EmployeException("Le matricule " + matricule + " n'existe pas !");
    }

    // Calcul de la performance
    Integer performance = calculerPerformance(employe.getPerformance(), caTraite, objectifCa);

    // Bonus si supérieur à la moyenne
    Double performanceMoyenne = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");
    if(performanceMoyenne != null && performance > performanceMoyenne){
        performance++;
    }

    // Affectation et sauvegarde
    employe.setPerformance(performance);
    employeRepository.save(employe);
}

// Méthode privée pour valider les paramètres
private void validateParamsPerformanceCommercial(String matricule, Long caTraite, Long objectifCa) throws EmployeException {
    if(caTraite == null || caTraite < 0){
        throw new EmployeException("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }
    if(objectifCa == null || objectifCa < 0){
        throw new EmployeException("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }
    if(matricule == null || !matricule.startsWith("C")){
        throw new EmployeException("Le matricule ne peut être null et doit commencer par un C !");
    }
}

// Méthode privée pour calculer la performance selon le CA traité et l'objectif
private Integer calculerPerformance(Integer performanceActuelle, Long caTraite, Long objectifCa) {
    Integer performance = Entreprise.PERFORMANCE_BASE;

    if(caTraite >= objectifCa*0.8 && caTraite < objectifCa*0.95){
        performance = Math.max(Entreprise.PERFORMANCE_BASE, performanceActuelle - 2);
    } else if(caTraite >= objectifCa*0.95 && caTraite <= objectifCa*1.05){
        performance = Math.max(Entreprise.PERFORMANCE_BASE, performanceActuelle);
    } else if(caTraite > objectifCa*1.05 && caTraite <= objectifCa*1.2){
        performance = performanceActuelle + 1;
    } else if(caTraite > objectifCa*1.2){
        performance = performanceActuelle + 4;
    }

    return performance;
}
