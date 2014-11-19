from django.conf.urls import patterns, include, url
from django.contrib import admin
from wardrive.logger import views

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'wardrive.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    # url(r'^admin/', include(admin.site.urls)),
    url(r'^save_data/', views.save_data),
    url(r'^read_data/', views.read_data),
    url(r'^$', views.index, name='index'),
)
