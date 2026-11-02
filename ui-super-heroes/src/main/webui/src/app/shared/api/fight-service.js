/**
 * Fight API — heroes/villains via Graft; battles via FightsGraftFacade when enabled.
 */

import axios from "axios"

import {getRandomHeroFromGraft} from "./heroes-graft-service"
import {getRandomVillainFromGraft} from "./villains-graft-service"
import {
  generateImageFromGraft,
  getFightsFromGraft,
  getRandomLocationFromGraft,
  narrateFightFromGraft,
  startFightFromGraft,
} from "./fights-graft-service"

const defaultHeaders = {'Content-Type': "application/json"}

let basePath = window?.APP_CONFIG?.API_BASE_URL
const calculateApiBaseUrl = window?.APP_CONFIG?.CALCULATE_API_BASE_URL

if (calculateApiBaseUrl) {
  basePath = window.location.protocol + "//" + window.location.host.replace('ui-super-heroes', 'rest-fights')
}

if (!basePath) {
  basePath = window.location.protocol + "//" + window.location.host
}

export async function getFights(options = {}) {
  try {
    const graftFights = await getFightsFromGraft(options)
    if (graftFights !== undefined) {
      return graftFights
    }
  }
  catch (error) {
    console.error(error)
  }

  const { page, size } = options
  const config = { headers: defaultHeaders }
  if (page !== undefined || size !== undefined) {
    config.params = {
      page: page ?? 0,
      size: size ?? 20,
    }
  }

  const response = await axios.get(`${basePath}/api/fights`, config)
  return response.data
}

export async function startFight(body) {
  if (body === null || body === undefined) {
    throw new Error('Required parameter body was null or undefined when calling startFight.')
  }

  try {
    const graftFight = await startFightFromGraft(body)
    if (graftFight !== undefined) {
      return graftFight
    }
  }
  catch (error) {
    console.error(error)
  }

  const response = await axios.post(
    `${basePath}/api/fights`, body, {
      crossDomain: true,
      defaultHeaders,
    }
  )
  return response.data
}

export async function getRandomLocation() {
  try {
    const location = await getRandomLocationFromGraft()
    if (location !== undefined) {
      return location
    }
  }
  catch (error) {
    console.error(error)
  }

  try {
    const response = await axios.get(`${basePath}/api/fights/randomlocation`, {
      headers: defaultHeaders,
    })
    return response.data
  }
  catch (error) {
    console.error(error)
  }
}

export async function getRandomFighters() {
  try {
    const [hero, villain] = await Promise.all([
      getRandomHeroFromGraft().catch(error => {
        console.error(error)
        return undefined
      }),
      getRandomVillainFromGraft().catch(error => {
        console.error(error)
        return undefined
      }),
    ])

    if (!hero && !villain) {
      return undefined
    }

    return {hero, villain}
  }
  catch (error) {
    console.error(error)
  }
}

export async function narrateFight(body) {
  if (body === null || body === undefined) {
    throw new Error('Required parameter body was null or undefined when calling narrateFight.')
  }

  try {
    const narration = await narrateFightFromGraft(body)
    if (narration !== undefined) {
      return narration
    }
  }
  catch (error) {
    console.error(error)
  }

  const headers = {...defaultHeaders, 'Accept': 'text/plain'}

  const response = await axios.post(`${basePath}/api/fights/narrate`,
    body,
    {
      crossDomain: true,
      responseType: 'text',
      headers,
    }
  )

  return response.data
}

export async function generateImage(narration, winnerPictureUrl, loserPictureUrl) {
  if (narration === null || narration === undefined) {
    throw new Error('Required parameter narration was null or undefined when calling generateImage.')
  }

  try {
    const image = await generateImageFromGraft(narration, winnerPictureUrl, loserPictureUrl)
    if (image !== undefined) {
      return image
    }
  }
  catch (error) {
    console.error(error)
  }

  const headers = {...defaultHeaders, 'Accept': 'application/json'}

  const response = await axios.post(`${basePath}/api/fights/narrate/image`,
    { narration, winnerPictureUrl, loserPictureUrl },
    {
      crossDomain: true,
      headers,
      timeout: 120000,
    }
  )

  return response.data
}
