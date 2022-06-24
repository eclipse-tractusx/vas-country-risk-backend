{{/*
Expand the name of the chart.
*/}}
{{- define "vas.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "vas.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "vas.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "vas.labels" -}}
helm.sh/chart: {{ include "vas.chart" . }}
{{ include "vas.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "vas.selectorLabels" -}}
app.kubernetes.io/name: {{ include "vas.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create name of application secret
*/}}
{{- define "vas.applicationSecret.name" -}}
{{- printf "%s-application" (include "vas.fullname" .) }}
{{- end }}

{{/*
Invoke include on given definition with postgresql dependency context
Usage: include "includeWithPostgresContext" (list $ "your_include_function_here")
*/}}
{{- define "includeWithPostgresContext" -}}
{{- $ := index . 0 }}
{{- $function := index . 1 }}
{{- include $function (dict "Values" $.Values.postgres "Chart" (dict "Name" "postgres") "Release" $.Release) }}
{{- end }}

{{/*
Invoke include on given definition with elastic dependency context
Usage: include "includeWithElasticContext" (list root "your_include_function_here")
*/}}
{{- define "includeWithElasticContext" -}}
{{- $ := index . 0 }}
{{- $function := index . 1 }}
{{- include $function (dict "Values" $.Values.elastic "Chart" (dict "Name" "elastic") "Release" $.Release) }}
{{- end }}
