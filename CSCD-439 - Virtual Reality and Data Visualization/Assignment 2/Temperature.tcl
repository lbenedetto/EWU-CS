package require vtk
package require vtkinteraction

vtkRenderer aRenderer
vtkRenderWindow renWin
  renWin AddRenderer aRenderer
vtkRenderWindowInteractor iren
  iren SetRenderWindow renWin

vtkStructuredPointsReader reader
  reader SetFileName "temperature.dat"

vtkContourFilter contours
  contours SetInputConnection [reader GetOutputPort]
  contours SetValue 0 0.5

vtkPolyDataMapper contMapper
  contMapper SetInputConnection [contours GetOutputPort]
  eval contMapper SetScalarRange[[reader GetOutput] GetScalarRange]

vtkActor skin
  skin SetMapper contMapper

vtkOutlineFilter outlineData
  outlineData SetInputConnection [reader GetOutputPort]

vtkPolyDataMapper mapOutline
  mapOutline SetInputConnection [outlineData GetOutputPort]


vtkActor outline
  outline SetMapper mapOutline
  [outline GetProperty] SetColor 1 1 1

vtkLookupTable bwLut
  bwLut SetSaturationRange 1 1
  bwLut SetHueRange  0.667 0
  bwLut SetValueRange  1 1 

vtkLookupTable hueLut
  hueLut SetHueRange  0.667 0
  hueLut SetSaturationRange 1 1
  hueLut SetValueRange 1 1
  hueLut Build

vtkLookupTable satLut
  satLut SetHueRange  0.667 0
  satLut SetSaturationRange  1 1
  satLut SetValueRange  1 1

vtkScalarBarActor scalarBar
  scalarBar SetLookupTable hueLut
  scalarBar SetTitle "Temperature"
  scalarBar SetNumberOfLabels 8
  scalarBar SetWidth 0.15
  scalarBar SetHeight 0.9

vtkImageMapToColors sagittalColors
  sagittalColors SetInputConnection [reader GetOutputPort]
  sagittalColors SetLookupTable bwLut
vtkImageActor sagittal
  [sagittal GetMapper] SetInputConnection [sagittalColors GetOutputPort]
  sagittal SetDisplayExtent 0 17  0 17  5 5

vtkImageMapToColors axialColors
  axialColors SetInputConnection [reader GetOutputPort]
  axialColors SetLookupTable hueLut
vtkImageActor axial
  [axial GetMapper] SetInputConnection [axialColors GetOutputPort]
  axial SetDisplayExtent 0 17  8 8  0 9

vtkImageMapToColors coronalColors
  coronalColors SetInputConnection [reader GetOutputPort]
  coronalColors SetLookupTable satLut
vtkImageActor coronal
  [coronal GetMapper] SetInputConnection [coronalColors GetOutputPort]
  coronal SetDisplayExtent 8 8  0 17  0 9

vtkCamera aCamera
  aCamera SetViewUp  0 0 -1
  aCamera SetPosition  0 1 0
  aCamera SetFocalPoint  0 0 0
  aCamera ComputeViewPlaneNormal

aRenderer AddActor outline
aRenderer AddActor sagittal
aRenderer AddActor axial
aRenderer AddActor coronal
aRenderer AddActor skin
aRenderer AddActor scalarBar

aRenderer SetActiveCamera aCamera
aRenderer ResetCamera
aCamera Dolly 1

aRenderer SetBackground 0 0.15 0.22
renWin SetSize 640 480

aRenderer ResetCameraClippingRange

iren AddObserver UserEvent {wm deiconify .vtkInteract}

iren Initialize
wm withdraw .
iren Start
