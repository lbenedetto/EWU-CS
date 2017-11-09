package require vtk
package require vtkinteraction

vtkRenderer aRenderer
vtkRenderWindow renWin
  renWin AddRenderer aRenderer
vtkRenderWindowInteractor iren
  iren SetRenderWindow renWin

vtkStructuredPointsReader reader
  reader SetFileName "pressure.dat"

vtkTextActor textRangeLabel
  #textRangeLabel SetInput "ISO Value: -0.18"
  #textRangeLabel SetInput "ISO Value: -0.1"
  #textRangeLabel SetInput "ISO Value: 0.0"
  #textRangeLabel SetInput "ISO Value: 0.1"
  textRangeLabel SetInput "ISO Value: 0.18"

vtkContourFilter contours
  contours SetInputConnection [reader GetOutputPort]
  #contours SetValue 0 -0.18
  #contours SetValue 0 -0.1
  #contours SetValue 0 0.0
  #contours SetValue 0 0.1
  contours SetValue 0 0.18

vtkOutlineFilter outlineData
  outlineData SetInputConnection [reader GetOutputPort]

vtkPolyDataMapper mapOutline
  mapOutline SetInputConnection [outlineData GetOutputPort]

vtkActor outline
  outline SetMapper mapOutline
  [outline GetProperty] SetColor 1 1 1

vtkLookupTable hueLut
  hueLut SetHueRange 0 0.667
  hueLut SetSaturationRange 1 1
  hueLut SetValueRange 1 1
  hueLut Build

vtkPolyDataMapper contMapper
  contMapper SetInputConnection [contours GetOutputPort]
  contMapper SetLookupTable hueLut
  contMapper SetScalarRange -0.2 0.2

vtkActor skin
  skin SetMapper contMapper

vtkScalarBarActor scalarBar
  scalarBar SetLookupTable hueLut
  scalarBar SetTitle "Pressure"
  [scalarBar GetPositionCoordinate] SetValue 0.01 0.1
  scalarBar SetNumberOfLabels 5
  scalarBar SetWidth 0.15
  scalarBar SetHeight 0.9

vtkCamera aCamera
  aCamera SetViewUp  0 0 -1
  aCamera SetPosition  0 1 0
  aCamera SetFocalPoint  0 0 0
  aCamera ComputeViewPlaneNormal

aRenderer AddActor scalarBar
aRenderer AddActor outline
aRenderer AddActor skin
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
