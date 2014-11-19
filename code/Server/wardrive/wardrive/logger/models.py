from django.db import models

# Create your models here.
class Statistics(models.Model):
    IMEI = models.CharField(max_length=100)
    IMSI = models.CharField(max_length=100)
    PHONE_MODEL = models.CharField(max_length=100)
    SIM_SN = models.CharField(max_length=100)
    GSM_TYPE = models.CharField(max_length=100)
    NETWORK_MCC = models.CharField(max_length=100)
    NETWORK_MNC = models.CharField(max_length=100)
    NETWORK_NAME = models.CharField(max_length=100)
    NETWORK_COUNTRY = models.CharField(max_length=100)
    NETWORK_TYPE = models.CharField(max_length=100)
    CELL_ID = models.CharField(max_length=100)
    CELL_PSC = models.CharField(max_length=100)
    CELL_LAC = models.CharField(max_length=100)
    RSSI = models.CharField(max_length=100)
    GPS = models.CharField(max_length=100)
    COLLECTED_TIME = models.CharField(max_length=100)
    SAVED_TIME = models.DateField(db_index=True, auto_now_add=True)
