export function doesJsonHave(json, fHandleMissingProperties, ...requiredProperties) { // example: if (checkReqBodyToContain(req, res, 'name', 'email', 'password'))
    var missingProperties = [];
    for (const prop of requiredProperties) {
      if (!(prop in json)) {
        missingProperties.push(prop);
      }
    }
    if (missingProperties.length > 0) {
      fHandleMissingProperties(missingProperties);
      return false;
    }
    return true;
}

export function handleMissingProperties(missingProperties){
    if (Array.isArray(missingProperties)){
      // res.status(400).json({msg:`Missing required properties in req.body: ${missingProperties.join(', ')}`})
    }
}

export function reqNotifier(req) {
    console.log('');
    console.log(`${req.method} request received for: ${req.originalUrl}`);
    console.log(`request body: ${JSON.stringify(req.body)}`);
}

export function logJson(json) {
    if (Array.isArray(json)) {
      console.log('[');
      json.forEach((element) => {
        console.log(`${JSON.stringify(element)},`);
      });
      console.log(']');
    } else {
      console.log(JSON.stringify(json));
    }
}