package require vtk
package require vtkinteraction

vtkRenderer aRenderer
vtkRenderWindow renWin
  renWin AddRenderer aRenderer
vtkRenderWindowInteractor iren
  iren SetRenderWindow renWin

vtkStructuredPointsReader reader
  reader SetFileName "temperature.dat"

vtkOutlineFilter outlineData
  outlineData SetInputConnection [reader GetOutputPort]

vtkPolyDataMapper mapOutline
  mapOutline SetInputConnection [outlineData GetOutputPort]

vtkTextActor textRangeLabel
  #textRangeLabel SetInput "Plane: X"
  #textRangeLabel SetInput "Plane: Y"
  textRangeLabel SetInput "Plane: Z"

vtkActor outline
  outline SetMapper mapOutline
  [outline GetProperty] SetColor 1 1 1

vtkLookupTable hueLut
  hueLut SetHueRange 0 0.667
  hueLut SetSaturationRange 1 1
  hueLut SetValueRange 1 1
  hueLut Build

vtkScalarBarActor scalarBar
  scalarBar SetLookupTable hueLut
  scalarBar SetTitle "Temperature"
  [scalarBar GetPositionCoordinate] SetValue 0.01 0.1
  scalarBar SetNumberOfLabels 5
  scalarBar SetWidth 0.15
  scalarBar SetHeight 0.9

vtkImageMapToColors sagittalColors
  sagittalColors SetInputConnection [reader GetOutputPort]
  sagittalColors SetLookupTable hueLut
vtkImageActor sagittal
  [sagittal GetMapper] SetInputConnection [sagittalColors GetOutputPort]
  #X
  #sagittal SetDisplayExtent 8 8  0 17  0 9
  #Y
  #sagittal SetDisplayExtent 0 17  8 8  0 9
  #Z
  sagittal SetDisplayExtent 0 17  0 17  5 5

vtkCamera aCamera
  aCamera SetViewUp  0 0 -1
  aCamera SetPosition  0 1 0
  aCamera SetFocalPoint  0 0 0
  aCamera ComputeViewPlaneNormal

aRenderer AddActor scalarBar
aRenderer AddActor outline
aRenderer AddActor sagittal
aRenderer AddActor textRangeLabel

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
