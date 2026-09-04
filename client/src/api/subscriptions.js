
const SubscriptionUrl = '/api/subscriptions';


export async function fetchData() {
            setLoading(true);
            try {
                const data = await getSubscriptions();
                setSubscriptions(data);
            } catch(err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
    }

/**
 * Sends request to create a new subscription
 */
export async function postSubscription(email) {
    try {
        const response = await fetch(SubscriptionUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email })
        });
        
        const success = response.ok;
        const data = await response.json();
        const { message } = data;

        return { success, message };

    } catch (err) {
        console.error('Error during post subscription request!');
        throw err;
    }
}

/**
 * Gets a list of all subscriptions currently stored
 */
export async function getSubscriptions() {
    try {
        const response = await fetch(SubscriptionUrl, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        // protect against transport layer failures
        if (!response.ok) {
            throw new Error("Something odd happened. Please refresh the page!");
        }
        const data = await response.json();
        return data ;

    } catch (err) {
        console.error('Error during get subscriptions request');
        throw err;
    }
}