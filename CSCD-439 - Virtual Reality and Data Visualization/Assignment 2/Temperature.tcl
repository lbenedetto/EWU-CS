package require vtk
package require vtkinteraction

vtkRenderer aRenderer
vtkRenderWindow renWin
  renWin AddRenderer aRenderer
vtkRenderWindowInteractor iren
  iren SetRenderWindow renWin

vtkStructuredPointsReader reader
  reader SetFileName "temperature.dat"

vtkContourFilter contour
  contour SetInputConnection [reader GetOutputPort]
  eval contour GenerateValues 100 [[reader GetOutput] GetScalarRange]

vtkPolyDataMapper contMapper
  contMapper SetInputConnection [contour GetOutputPort]
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

vtkCamera aCamera
  aCamera SetViewUp  0 0 -1
  aCamera SetPosition  0 1 0
  aCamera SetFocalPoint  0 0 0
  aCamera ComputeViewPlaneNormal

aRenderer AddActor scalarBar
aRenderer AddActor outline
aRenderer AddActor skin

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
