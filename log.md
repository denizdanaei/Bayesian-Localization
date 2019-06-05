**Samples from one access point**
    We measured the signal strength from a single access
    point over a five minute period. We took the samples one
    second apart for a total of 300 samples. the histogram range can be as large as
    10 dBm or more. 

    during the online phase, the system should use
    more than one sample in the estimation process to have a
    better estimate of the signal strength at a location.
**Small-Scale Variations**
    These variations happen when the user moves over a
    small distance (order of wavelength). This leads to
    changes in the average received signal strength. For the
    802.11b networks working at the 2.4 GHz range, the
    wavelength is 12.5 cm and we measure a variation in the
    average signal strength up to 10 dBm in a distance as
    small as 7.6 cm (3 inches)
#HORUS System

*Trainnig phase* 
    
**Clustering module** is used to group radio map locations based on
    the access points covering them. Clustering is used to reduce the
    computational requirements of the system and, hence, conserve power (Section 3.7).
**Discrete Space Estimator module** returns the radio map location 
    that has the maximum probability given the received signal strength
    vector from different access points (Section 3.3).
**Correlation Modelling and Handling modules** use an autoregressive model 
    to capture the correlation between consecutive samples from the same access point.
    This model is used to obtain a better discrete location estimate using the average
    of n correlated samples (Section 3.4).
**Continuous Space Estimator** takes as an input the discrete estimated user location, one of the radio map locations, and returns a        more accurate estimate of the user location in the continuous space (Section 3.5).  
**Small-Scale Compensator module** handles the small-scale variation characteristics of the wireless channel (Section 3.6).  


