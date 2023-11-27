## NOTICE

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

- SPDX-License-Identifier: Apache-2.0
- Licence Path: https://creativecommons.org/licenses/by/4.0/legalcode
- Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
- Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
- Source URL: https://github.com/eclipse-tractusx/vas-country-risk-backend




# User Guides

## Portal to Dashboard

In this Section we are describing how you can gain Access to the Dashboard
1.	First you need to create an account for the Catena-X Environment
2.	Your Company needs to setup everything for the Connection with our Application: This can be found with the BPDM 'Create a Gate'  
      And needs to be subscribed to the service

![Portal](../docs/User-Guide-Images/add_app.png)

3. Your Accounts needs to have one of the two Roles currently available

![App](../docs/User-Guide-Images/main_page.png)


After the Previous steps are finished the way to the Dashboard is very simple:

1. Go to the Catena-X Portal
2. Open your Applications
3. Click on the Country Risk Dashboard
4. Afterwards you will be sent to the Country Risk Dashboard Login  
   You will be asked to Login

![Login](../docs/User-Guide-Images/image2023-1-16_11-0-2.png)

Choose the Login Option you want and its setup.

![Input](../docs/User-Guide-Images/login_board.png)

5. After the Login you will be directed to the Dashboard

![Dashboard](../docs/User-Guide-Images/Dashboard.png)


## Country Rating Component

![Rating](../docs/User-Guide-Images/RatingList.png)

This Component includes multiple smaller Functionalities

- Year Selection
- Gate Selection
- Show More Ratings
- Upload Rating
- Download Template
- Weighting of Ratings
- Deletion of Ratings

The first step you need to take is to select a year.

![Year](../docs/User-Guide-Images/RatingYearSelection.png)

After you selected a year the Rating List will be updated. The Ratings in the List are saved for the selected year.  
Currently all Globally available Ratings are Open Source. We do not create Scores, we only collect them and aggregate them according to your selections.

![RatingTable](../docs/User-Guide-Images/RatingSelection.png)

Next to the Year Selection you will find the Gate selection. This selection will be done for you atuomatically on the first start-up of the Dashbaord.

![Gateselect](../docs/User-Guide-Images/GateSelection.png)

The Gate gives the Input on which Business Partners you will be able to see in all components.  
Thereafter you can select one or multiple Ratings in the Table.

![Ratingselected](../docs/User-Guide-Images/RatingSelected.png)

If you want to select more Ratings at once you can Open a Pop-Up with the 'Show Ratings' Button  
In this Table you can manipulate the same way you can in the smaller Table.

![Popupselection](../docs/User-Guide-Images/RatingListPopUp.png)


After selecting a Rating the World Map will change the Color of each individual Country.  
How is this done. Each Rating has a score for most of the Countries. We have a Score Range of 0-100 for each Rating  
For Example the CPI Rating has a Score of 45 for China and 29 for Russia.  
Depending on your selection with the Ranges the Country is then colored.

![WorldMap](../docs/User-Guide-Images/WorldMapFiltered.png)

As you can see the Weighting column to the right of the Names are automatically created and evenly distributed.  
This weighting can be customized individually.

The Rules for the weighting are:
1. always needs to add up to 100%

![weighting](../docs/User-Guide-Images/WeightingOverlap.png)

2. automatic weighting overwrites upon selecting new Raings
3. when changing you need to confirm value by pressing "enter"
   Changing the Weighting changes the Scores given in the Table below. How the Scores are calculated is described there.


### How can i Upload my own Rating and what do i need to know.

The First step is to Download the Template. This is important since this is the Only Format the Application can progress the Upload.

![excelfile](../docs/User-Guide-Images/image2023-1-16_11-43-52.png)

![excellist](../docs/User-Guide-Images/image2023-1-16_11-44-50.png)

After filling out the Column E please save the File. The name is not important since the Name is set when the Rating is Uploaded.  
After creating your own Rating all you have to do is Upload the File, you can start with clicking on 'Upload Rating'

Then this Pop-Up is shown:

![popupupload](../docs/User-Guide-Images/RatingUpload.png)

You then have the Option to select who can see this Rating after Uploading.  
You have the Option between 'Only For me' or 'For the Company'. The secon Option is grayed out if you dont have administration rights.  
After selecting the Target group you can select the year for which this Rating is relevant for.  
Below the year selection you can Give a Name to the Rating. A Rating Name can only be given once.  
This Name is then shown in the Rating table after finishing the Upload.


![savingrating](../docs/User-Guide-Images/RatingUploadFilled.png)

Afterwards yoou will be asked to Upload the File you previously created. You can do thi s per darg and drop or by clicking on the cloud symbol to search your Explorer for the File.

![popupsaved](../docs/User-Guide-Images/RatingUploadSuccess.png)

After closing the Pop-Up the Rating is directly available in the table without the need to refresh the application.

![refresh](../docs/User-Guide-Images/RatingUploaded.png)

If you want to delete a Rating you have the Option to do so by clicking on the 'Trash' Icon to the right of the Rating.

![deleterating](../docs/User-Guide-Images/image2023-1-16_11-58-21.png)

and then finish by confirming it.



## World Map

The Map shows an interactive Map of the World for the User

- The User can zoom into the Map
- After the first Zoom step the country Codes are shown with the ISO 3166-1 alpha-2 Codes
- Hovering over the Codes shows the Complete Country Name

![hovercountry](../docs/User-Guide-Images/WorldMap.png)

- Upon zooming in the User can see Markers in countries.
  Those markers show the Location of the available Business Partners
  Hovering over the Marker reveals the Company Name in that Location

![hoveringmarker](../docs/User-Guide-Images/WorldMapMarker.png)

- The User has the Option to Expand the Map with the Help of the Icon in the top right corner
  After Expanding the World Map the User can find a Legend in the bottom left corner which represents the Ranges
  The functionalities remain in the expanded Mode

![expandbutton](../docs/User-Guide-Images/WorldMapExpand.png)

![popupMap](../docs/User-Guide-Images/WorldMapExpanded.png)

- After Expanding the World Map the User can see the Option to Export the Image.
  Upon clicking on the Button a PNG is created and downloaded. The File is named "worldMap.png"

![exportImage](../docs/User-Guide-Images/WorldMapExport.png)


## Company View

The Company View shows a Map of the World.

The Map is similar to the World Map.  
In this Map the User can also zoom in to reveal more Information:

![zoommap](../docs/User-Guide-Images/CompanyView.png)

The User can also hover over one Country at a time.

![hovercountrymap](../docs/User-Guide-Images/CompanyViewHover.png)

In the Company view we not only show the Name of the Country we also show how many Business Partners which are available to you are located in this country.  
We also show the Score in the Hover since the Countries are not being colored when a Rating is selected.

New in this View is the Option to Filter for a country

![searchcountry](../docs/User-Guide-Images/CompanyViewFiltered.png)

After selecting a country the Map zooms to the country and shows the Markers for each Business Partner in this Country.  
Also the Table below is filtered for the selected country.

![tablesearched](../docs/User-Guide-Images/TableFiltered.png)

The User has the Option to Expand the Map with the Help of the Icon in the top right corner.  
The functionalities remain in the expanded Mode.

![expandcountrymap](../docs/User-Guide-Images/CompanyViewExpand.png)

![expandedcountrymap](../docs/User-Guide-Images/ComapnyViewExpanded.png)

After Expanding the Map the User can see the Option to Export the Image.  
Upon clicking on the Button a PNG is created and downloaded. The File is named "BusinessPartnerMap.png"


## Business Partner Table

![Table](../docs/User-Guide-Images/Table.png)

Functionalities:

- Search in every Column
- On the Fly calculated Score
- Sorting Desc/Asc
- Pagination
- KPI for filtered/unfiltered number of Business Partners
- Export of Table as a CSV File (depending on the selected Business partners)


In the Table we show 7 Columns:

- Business Partner Number
- Legal Name
- Country
- City
- Score
- Ratings (In the Rating Column we then show the selected Ratings.)

In the Table you can also have 2 more columns dependent of the roles you have:
- Customer
- Supplier


Upon starting the Application we are setting up a connection to the Gate your Company has setup and you are allowed to access.  
We then make a request to get the Information for each BPN-L, BPN-S and BPN-A.  
We then display all Business Partners in the table and the World Map.

![tablefull](../docs/User-Guide-Images/tablefull.png)


### Pagination

On each Page we show 15 Business partners. If the User wants to see the next 15 business Partners he has the Option to switch to the next Page by clicking in the bottom right corner on the arrows

![pagination](../docs/User-Guide-Images/TablePagination.png)

### The KPI
We show the number of filtered Business Partners next to the Export Option:

![kpi](../docs/User-Guide-Images/TableFilteredBusinessPartners.png)

### The Score

If the User selects a Rating we create a Score depending on his selection in the Rating Table and Color the background according to the Ranges set.

The Score in the Column "Score" is calculated as follows.

Situation 1:  
You choose 1 Rating. The Score is taken from the database and shown. If there is none the field will be left empty. The Colore is chosen depending on the Ranges

Situation 2:  
You choose more than 1 Rating. The Formula: (Rating1 * Weighting1) + (Rating2 + Weighting2) + ... as many Ratings as you chose.

Situation 3:  
You choose more than 1 rating but there is only one Score.  
Then the Formula looks like this: (Rating1(available) * Weighting1(100%)) + (Rating2(n/A) + Weighting2(0%)) + ...


### Filtering of the Table

The User has the option to Filter in the table by Clicking on the Magnifying glas in the top right corner of the Table.  
This search is looking in each column for the entered text.

![searchexample](../docs/User-Guide-Images/image2023-1-16_13-3-10.png)

### Export of the selected Table

The User has the option to Export the Table and its content.

1. The User selects which rows to export, each line individually or all rows

![checkbox](../docs/User-Guide-Images/TableSelection.png)

2. The User then clicks on 'Export to csv'

![exportbutton](../docs/User-Guide-Images/TableExport.png)

3. After that a CSV File is created, which you can then Download.

![exportedfile](../docs/User-Guide-Images/image2023-1-16_13-7-35.png)


## Reports

![reports](../docs/User-Guide-Images/Reports.png)

Functionalities:

- Creating A Report
- Showing a Report
- Changing a Report
- Delete a Report
- Share a Report

### How to create a Report:

To create a Report the User can set multiple Filters:

- Ratings selected
- Weighting set
- Country Filtered in the Company View
- Table Filter
- Ranges Set


When the User has set a view of the Dashboard he wants to acces regularly without the need to recreate it everytime he can save a report:

1. Click on 'Save Report'

![savereport](../docs/User-Guide-Images/ReportSave.png)

2. Fill out the Pop-Up
   The User can choose between Uploading a Report for himself or for the Company if he has the status of an admin

![savereportpopup](../docs/User-Guide-Images/ReportCreated.png)

After setting a Name and clicking on Save the Report is then available in the Report Table

![definitionreport](../docs/User-Guide-Images/ReportAdded.png)


### How to change a Saved report
The process of changing a saved Report is eay.

1. Select the Report

![selectreport](../docs/User-Guide-Images/ReportSelected.png)

2. Change the Filters in the Dashboard

![changes1](../docs/User-Guide-Images/ReportChange.png)

3. Click on the Save Icon next to the selected Report

![saveicon](../docs/User-Guide-Images/ReportSaveIcon.png)

4. Confirm that you want to save the changes

![confirmation](../docs/User-Guide-Images/ReportChanged.png)


### How to Share a Report with a colleague

To Share a Report with a Colleague, the Perosn needs to be known to our application.
Meaning he has to have created a Report or a Rating for himself.

ToShare a Report you then select the Report you want to share and continue by clicking on the Share Icon to the most right of the Row.

![shareicon](../docs/User-Guide-Images/ReportShareIcon.png)

In the Dropdown you select the Person you want to share the Report with and click on 'share'

![selectusers](../docs/User-Guide-Images/ReportShareSelection.png)

You can only share Reports with Users in the Same Company as you.


### How to delete a Report

You can delete a Report by clicking on the 'trash' icon between the Save and Share functionality

![deleteicon](../docs/User-Guide-Images/ReportDeleteIcon.png)

Afterwards you confirm that you want to delete the Report by clicking on 'yes'

![confirm](../docs/User-Guide-Images/ReportDeleteConfirmation.png)


## Rating Range Setter

The Ranges define which color is shown for each Score

![rangesselector](../docs/User-Guide-Images/draganddropranges.png)

The Standard values are:  
Green: 100 - 71  
Yellow: 70 - 31  
Red: 30 - 0


### How to set Ranges

The User can slide the endpoint Points of the Strings by dragging them per Mouse hold.

![draganddrop](../docs/User-Guide-Images/draganddropranges.png)

The other option is by inputing the Value into the Text fields next to the Sliders and confirming by either clicking out of the box or pressing enter.

![inputfield](../docs/User-Guide-Images/rangessaved.png)

The Values automatically adjust to the changes. There is not an option to have overlapping Values.

![overlap](../docs/User-Guide-Images/rangesoverlap.png)

### How To Save individual Ranges

To save the Ranges the User can click on 'Save Ranges' they are then automatically set to the currently selected values.

![savebutton](../docs/User-Guide-Images/savebuttonranges.png)

![informationof saving](../docs/User-Guide-Images/rangessaved.png)

When the User Returns at a later date the Saved Ranges are automatically set.