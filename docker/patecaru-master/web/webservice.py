import cron, schedule, bottle, muesli

app = bottle.Bottle()

@app.get('/muesli')
def status():
  return muesli.status()

@app.post('/muesli')
def kaffee():


def scheduleCleanAndSetup():

if __name__ == "__main__":
  scheduleRunner = cron.ScheduleRunner()
  scheduleRunner.daemon = True
  scheduleRunner.start()
  scheduleCleanAndSetup()
  bottle.run(app, server='cherrypy', host='0.0.0.0', port=80, quiet=True)

