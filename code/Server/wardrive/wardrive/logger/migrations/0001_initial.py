# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='MCC_MNC',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('NETWORK_MCC', models.CharField(max_length=100)),
                ('NETWORK_MNC', models.CharField(max_length=100)),
                ('NETWORK_ISO', models.CharField(max_length=100)),
                ('NETWORK_COUNTRY', models.CharField(max_length=100)),
                ('NETWORK_COUNTRYCODE', models.CharField(max_length=100)),
                ('NETWORK_NAME', models.CharField(max_length=100, null=True, blank=True)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Statistics',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('IMEI', models.CharField(max_length=100, null=True)),
                ('IMSI', models.CharField(max_length=100, null=True)),
                ('PHONE_MODEL', models.CharField(max_length=100, null=True)),
                ('SIM_SN', models.CharField(max_length=100, null=True)),
                ('GSM_TYPE', models.CharField(max_length=100, null=True)),
                ('NETWORK_MCC', models.CharField(max_length=100, null=True)),
                ('NETWORK_MNC', models.CharField(max_length=100, null=True)),
                ('NETWORK_NAME', models.CharField(max_length=100, null=True)),
                ('NETWORK_COUNTRY', models.CharField(max_length=100, null=True)),
                ('NETWORK_TYPE', models.CharField(max_length=100, null=True)),
                ('CELL_ID', models.CharField(max_length=100, null=True)),
                ('CELL_PSC', models.CharField(max_length=100, null=True)),
                ('CELL_LAC', models.CharField(max_length=100, null=True)),
                ('RSSI', models.CharField(max_length=100, null=True)),
                ('GPS', models.CharField(max_length=100, null=True)),
                ('COLLECTED_TIME', models.CharField(max_length=100, null=True)),
                ('SAVED_TIME', models.DateField(auto_now_add=True, db_index=True)),
                ('OPENCELLID_STATUS', models.BooleanField(default=False)),
                ('OPENCELLID_LOG', models.TextField(null=True)),
                ('MCCMNC_STATUS', models.BooleanField(default=False)),
                ('MCCMNC_LOG', models.TextField(null=True)),
                ('CHECK_STATUS', models.BooleanField(default=False)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
