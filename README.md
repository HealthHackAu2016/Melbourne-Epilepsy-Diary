# What is the problem?
People with epilepsy have many more seizures that they are typically able to report using seizure diaries. This can lead to difficulty in understanding their condition and prescribing the best medication and care for them. In addition, it is difficult for pharmaceutical companies to evaluate new medications as there can be a lack of reliability in the reporting of seizures in the clinical trials.

# Are there any existing solutions?
Patients usually record their seizures on paper or in electronic form, but this requires them or their carers to do this.

# What would be the ideal solution, given a big budget and lots of time?
A mobile phone or other device that can tell if a person is having a seizure and record it for reporting to the patient's clinician either immediately or on a regular basis. Ideally, this device should record the situation in a way that allows for determination of false positives, which can thus enable higher sensitivity.

# What can be done in 48 hours at a hackathon?
A prototype that the detects shaking, turns on the microphone for a while to record local activity and logs the event would be a great proof of concept that would help further our work and be useful in short term use.

# Solution

1. To using Android sensor to collect users shaking data.
2. Every 30 seconds, send the device local temporary data to server side and store.
3. Every 30 seconds, server side clean the raw data. (Median Filter & 3rd Order Butterworth Filter)
4. Then using Machine learning to detect whether the user is in a emergency situation.
5. If yes, send the signal to related organization.

## How it works?

![Image](http://remo.site/img/image.png)
